package org.tybaco.ui.child.project.diagram.line;

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

import javafx.beans.*;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.tybaco.ui.child.project.diagram.Diagram;
import org.tybaco.ui.model.Link;
import org.tybaco.ui.util.CurveDivider;

import java.awt.geom.Rectangle2D;

public class DiagramLine extends Group {

  private static final CurveDivider D5 = new CurveDivider(5);
  private static final double D = 6d;
  private static final double H = 3d;
  private final InvalidationListener boundsInvalidationListener = this::onUpdate;
  public final Diagram diagram;
  public final Link link;
  private final MoveTo startPoint = new MoveTo();
  private final LineTo startConnector = new LineTo();
  private final CubicCurveTo curve = new CubicCurveTo();
  private final LineTo endConnector = new LineTo();
  private final Path path = new Path(startPoint, startConnector, curve, endConnector);

  private double xs;
  private double ys;
  private double xe;
  private double ye;

  static Color color = Color.RED;

  public DiagramLine(Diagram diagram, Link link) {
    this.diagram = diagram;
    this.link = link;
    visibleProperty().bind(link.input.isNotNull().and(link.output.isNotNull()).and(link.inBounds.isNotNull()).and(link.outBounds.isNotNull()));
    getChildren().add(path);
    path.setStrokeWidth(2d);
    path.setStroke(Color.WHITE);
    path.setStrokeLineJoin(StrokeLineJoin.ROUND);
    path.setFill(null);
    initialize();
  }

  private void initialize() {
    var wil = new WeakInvalidationListener(boundsInvalidationListener);
    link.inBounds.addListener(wil);
    link.outBounds.addListener(wil);
    diagram.diagramBlockBoundsObserver.addListener(wil);
    onUpdate(null);
  }

  private void onUpdate(Observable o) {
    if (!isVisible()) {
      return;
    }
    var ib = link.inBounds.get();
    var ob = link.outBounds.get();
    if (ib != null && ob != null) {
      onUpdate(ib, ob);
    }
  }

  private void onUpdate(Bounds ib, Bounds ob) {
    xs = ob.getMaxX() + D;
    ys = ob.getCenterY();
    xe = ib.getMinX() - D;
    ye = ib.getCenterY();
    if (new SimpleLine(this).tryApply(xs, ys, xe, ye)) {
      return;
    } else if (new InnerLine(this).tryApply(xs, ys, xe, ye)) {
      return;
    } else if (new OuterLine(this).tryApply(xs, ys, xe, ye)) {
      return;
    }
    path.setVisible(false);
  }

  boolean tryApply(double cx1, double cy1, double cx2, double cy2) {
    D5.divide(xs, ys, cx1, cy1, cx2, cy2, xe, ye);
    if (checkConstraint()) {
      startPoint.setX(xs - D + 2d);
      startPoint.setY(ys);
      startConnector.setX(xs);
      startConnector.setY(ys);
      curve.setControlX1(cx1);
      curve.setControlY1(cy1);
      curve.setControlX2(cx2);
      curve.setControlY2(cy2);
      curve.setX(xe);
      curve.setY(ye);
      endConnector.setX(xe + D - 2d);
      endConnector.setY(ye);
      path.setStroke(color);
      path.setVisible(true);
      return true;
    } else {
      return false;
    }
  }

  private boolean checkConstraint() {
    return diagram.diagramBlockBoundsObserver.testNoBounds(b -> {
      var r = new Rectangle2D.Double(b.getMinX() - H, b.getMinY() - H, b.getWidth() + D, b.getHeight() + D);
      return D5.intersects(r);
    });
  }
}
