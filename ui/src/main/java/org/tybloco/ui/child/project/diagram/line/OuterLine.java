package org.tybloco.ui.child.project.diagram.line;

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

import javafx.geometry.Bounds;

import static java.lang.Math.max;
import static java.lang.Math.min;

final class OuterLine {

  private static final int STEP = 30;

  static boolean ol(DiagramLine line, double ys, double ye) {
    var input = line.link.input.get();
    var output = line.link.output.get();
    var ib = line.diagram.diagramBlockBoundsObserver.get(input.block);
    var ob = line.diagram.diagramBlockBoundsObserver.get(output.block);
    if (ye > ys) {
      return bottom(line, ys, ib, ob) || top(line, ys, ib, ob);
    } else {
      return top(line, ys, ib, ob) || bottom(line, ys, ib, ob);
    }
  }

  private static boolean bottom(DiagramLine line, double ys, Bounds ib, Bounds ob) {
    double maxX = max(ib.getMaxX(), ob.getMaxX()), minX = min(ib.getMinX(), ob.getMinX());
    double maxY = max(ib.getMaxY(), ob.getMaxY());
    for (int i = 10; i < 30; i++) {
      double cx1 = maxX + i * STEP, cx2 = minX - i * STEP;
      for (int j = 0; j < 30; j++) {
        if (line.tryApply(cx1, ys + j * STEP, cx2, maxY + j * STEP))
          return false;
      }
    }
    return true;
  }

  private static boolean top(DiagramLine line, double ys, Bounds ib, Bounds ob) {
    double maxX = max(ib.getMaxX(), ob.getMaxX()), minX = min(ib.getMinX(), ob.getMinX());
    double minY = min(ib.getMinY(), ob.getMinY());
    for (int i = 10; i < 30; i++) {
      double cx1 = maxX + i * STEP, cx2 = minX - i * STEP;
      for (int j = 0; j < 30; j++) {
        if (line.tryApply(cx1, ys - j * STEP, cx2, minY - j * STEP))
          return false;
      }
    }
    return true;
  }
}
