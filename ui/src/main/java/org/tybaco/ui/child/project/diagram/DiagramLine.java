package org.tybaco.ui.child.project.diagram;

/*-
 * #%L
 * ui
 * %%
 * Copyright (C) 2023 Montoni
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.*;
import javafx.beans.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;
import org.tybaco.ui.model.Link;

import java.util.LinkedList;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.tybaco.ui.child.project.diagram.DiagramCalculations.boundsIn;

public class DiagramLine extends Group {

  private static final float SAFE_DIST = 3f;
  private static final boolean DEBUG = false;

  private final InvalidationListener boundsInvalidationListener = this::onUpdate;
  private final InvalidationListener connectorsInvalidationListener = this::onUpdateConnectors;
  final Link link;
  final Path path = new Path();

  public DiagramLine(Link link) {
    this.link = link;
    getChildren().add(path);
    path.setStrokeWidth(2d);
    path.setStroke(Color.WHITE);
    path.setStrokeLineJoin(StrokeLineJoin.ROUND);
    initialize();
  }

  private void initialize() {
    var cwl = new WeakInvalidationListener(connectorsInvalidationListener);
    link.input.addListener(cwl);
    link.output.addListener(cwl);
    link.inBounds.addListener(cwl);
    link.outBounds.addListener(cwl);
    cwl.invalidated(null);
  }

  private void onUpdateConnectors(Observable o) {
    var input = link.input.get();
    var output = link.output.get();
    var inBounds = link.inBounds.get();
    var outBounds = link.outBounds.get();
    if (input == null || output == null || inBounds == null || outBounds == null) {
      setVisible(false);
      return;
    }
    var wl = new WeakInvalidationListener(boundsInvalidationListener);
    link.inBounds.addListener(wl);
    link.outBounds.addListener(wl);
    setVisible(true);
    onUpdate(null);
  }

  private void onUpdate(Observable o) {
    var input = link.input.get();
    var output = link.output.get();
    var inBounds = link.inBounds.get();
    var outBounds = link.outBounds.get();
    if (input == null || output == null || inBounds == null || outBounds == null) {
      path.getElements().clear();
      return;
    }
    if (DEBUG) input.block.diagram.debugNodes.getChildren().removeIf(c -> c instanceof Rectangle);
    onUpdate(input, output, outBounds, inBounds);
  }

  private void onUpdate(DiagramBlockInput input, DiagramBlockOutput output, Bounds outBounds, Bounds inBounds) {
    if (trySimpleLine(input, output, inBounds, outBounds)) return;
    if (tryLineOI(input, output, inBounds, outBounds)) return;
    path.getElements().clear();
  }

  private boolean trySimpleLine(DiagramBlockInput i, DiagramBlockOutput o, Bounds ib, Bounds ob) {
    var xs = (float) ob.getMaxX();
    var ys = (float) ob.getCenterY();
    var xe = (float) ib.getMinX();
    var ye = (float) ib.getCenterY();
    if (xs < xe) {
      if (xe - xs >= 50f) {
        var dx = (xe - xs) / 5f;
        var shape = new CubicCurve2D(xs + SAFE_DIST, ys, xs + dx, ys, xe - dx, ye, xe - SAFE_DIST, ye);
        return tryApply(i, o, shape);
      }
    }
    return false;
  }

  private boolean tryLineOI(DiagramBlockInput i, DiagramBlockOutput o, Bounds ib, Bounds ob) {
    var xs = (float) ob.getMaxX();
    var ys = (float) ob.getCenterY();
    var xe = (float) ib.getMinX();
    var ye = (float) ib.getCenterY();
    var ub = ib.getMinY() < ob.getMinY() ? ib : ob;
    var lb = ib.getMinY() < ob.getMinY() ? ob : ib;
    if (lb.getMinY() > ub.getMaxY()) {
      var gapY = (float) (lb.getMinY() - ub.getMaxY());
      if (gapY > 50f) {
        var maxX = (float) (max(lb.getMaxX(), ub.getMaxX()) + ub.getWidth() * 13d);
        var ry = (float) (ub.getMinY() + gapY / 3f);
        var minX = (float) (min(lb.getMinX(), ub.getMinX()) - lb.getWidth() * 13d);
        var ly = (float) (lb.getMinY() - gapY / 3f);
        var shape = new CubicCurve2D(xs + SAFE_DIST, ys, maxX, ry, minX, ly, xe - SAFE_DIST, ye);
        return tryApply(i, o, divide(shape));
      }
    }
    return false;
  }

  private void apply(Shape... shapes) {
    var elems = new LinkedList<PathElement>();
    for (var shape : shapes) {
      switch (shape) {
        case Line2D l -> {
          elems.add(new MoveTo(l.x1, l.y1));
          elems.add(new LineTo(l.x2, l.y2));
        }
        case CubicCurve2D c -> {
          elems.add(new MoveTo(c.x1, c.y1));
          elems.add(new CubicCurveTo(c.ctrlx1, c.ctrly1, c.ctrlx2, c.ctrly2, c.x2, c.y2));
        }
        case QuadCurve2D c -> {
          elems.add(new MoveTo(c.x1, c.y1));
          elems.add(new QuadCurveTo(c.ctrlx, c.ctrly, c.x2, c.y2));
        }
        default -> {}
      }
    }
    path.getElements().setAll(elems);
  }

  private boolean tryApply(DiagramBlockInput input, DiagramBlockOutput output, Shape... shapes) {
    var needsApply = Stream.concat(blocks(input), connectors(input, output)).noneMatch(b -> {
      for (var s : shapes) {
        if (s.intersects((float) b.getMinX(), (float) b.getMinY(), (float) b.getWidth(), (float) b.getHeight())) {
          if (DEBUG) debug(input, b);
          return true;
        }
      }
      return false;
    });
    if (needsApply) {
      apply(shapes);
    }
    return needsApply;
  }

  private Stream<Bounds> blocks(DiagramBlockInput input) {
    var blocksBase = input.block.diagram.blocks;
    var stream = blocksBase.getChildren().stream();
    return (DEBUG ? stream : stream.parallel())
      .filter(Node::isVisible)
      .map(n -> boundsIn(blocksBase, n));
  }

  private Stream<Bounds> connectors(DiagramBlockInput input, DiagramBlockOutput output) {
    var connectorsBase = input.block.diagram.connectors;
    var stream = connectorsBase.getChildren().stream();
    return (DEBUG ? stream : stream.parallel())
      .filter(n -> !(n instanceof DiagramLine))
      .filter(Node::isVisible)
      .filter(n -> checkCompanion(n, input, output))
      .map(n -> boundsIn(connectorsBase, n));
  }

  private boolean checkCompanion(Node node, DiagramBlockInput input, DiagramBlockOutput output) {
    return switch (node) {
      case DiagramBlockInputCompanion c -> c.input != input;
      case DiagramBlockOutputCompanion c -> c.output != output;
      default -> true;
    };
  }

  private void debug(DiagramBlockInput input, Bounds b) {
    var r = new Rectangle(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    r.setFill(new Color(0.9, 0.3, 0.2, 0.2));
    input.block.diagram.debugNodes.getChildren().add(r);
  }

  private CubicCurve2D[] divide(CubicCurve2D c) {
    var cs = new CubicCurve2D[] {new CubicCurve2D(), new CubicCurve2D(), new CubicCurve2D(), new CubicCurve2D()};
    c.subdivide(cs[0], cs[2]);
    cs[0].subdivide(cs[0], cs[1]);
    cs[2].subdivide(cs[2], cs[3]);
    return cs;
  }
}
