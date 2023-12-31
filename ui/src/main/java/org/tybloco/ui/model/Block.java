package org.tybloco.ui.model;

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

import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public final class Block {

  public final int id;
  public final SimpleStringProperty name;
  public final String factoryId;
  public final SimpleDoubleProperty x;
  public final SimpleDoubleProperty y;
  private final Observable[] observables;

  Block(int id, String name, String factoryId, double x, double y) {
    this.id = id;
    this.name = new SimpleStringProperty(this, "name", name);
    this.factoryId = factoryId;
    this.x = new SimpleDoubleProperty(this, "x", x);
    this.y = new SimpleDoubleProperty(this, "y", y);
    this.observables = new Observable[] {this.name};
  }

  public Block(Element element) {
    this(
      parseInt(element.getAttribute("id")),
      element.getAttribute("name"),
      element.getAttribute("factoryId"),
      parseDouble(element.getAttribute("x")),
      parseDouble(element.getAttribute("y"))
    );
  }

  public void saveTo(Element element) {
    element.setAttribute("id", Integer.toString(id));
    element.setAttribute("name", name.get());
    element.setAttribute("factoryId", factoryId);
    element.setAttribute("x", Double.toString(x.get()));
    element.setAttribute("y", Double.toString(y.get()));
  }

  public static ObservableList<Block> newList(Collection<Block> blocks) {
    return FXCollections.observableList(new ArrayList<>(blocks), b -> b.observables);
  }
}
