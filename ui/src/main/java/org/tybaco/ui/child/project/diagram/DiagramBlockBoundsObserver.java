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

import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.tybaco.editors.util.InvalidationListeners;

import java.util.IdentityHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class DiagramBlockBoundsObserver extends InvalidationListeners {

  private final IdentityHashMap<Node, Bounds> bounds = new IdentityHashMap<>(128);

  public DiagramBlockBoundsObserver(Pane pane) {
    pane.getChildren().forEach(n -> bounds.put(n, n.getBoundsInParent()));
    pane.getChildren().addListener((ListChangeListener<? super Node>) c -> {
      while (c.next()) {
        if (c.wasRemoved()) {
          c.getRemoved().forEach(bounds::remove);
        }
        if (c.wasAdded()) {
          for (var n : c.getAddedSubList()) {
            bounds.put(n, n.getBoundsInParent());
            n.boundsInParentProperty().addListener((o, ov, nv) -> {
              bounds.put(n, nv);
              fire();
            });
          }
        }
        fire();
      }
    });
  }

  public Stream<Bounds> bounds() {
    return bounds.values().stream();
  }

  public boolean testNoBounds(Predicate<Bounds> predicate) {
    for (var b : bounds.values()) {
      if (predicate.test(b)) {
        return false;
      }
    }
    return true;
  }
}
