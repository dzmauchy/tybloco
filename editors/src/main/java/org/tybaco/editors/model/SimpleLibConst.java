package org.tybaco.editors.model;

/*-
 * #%L
 * editors
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

import com.github.javaparser.ast.expr.Expression;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.tybaco.editors.control.GridPanes;
import org.tybaco.editors.dialog.SimpleModalDialog;
import org.tybaco.editors.util.SeqMap;

import java.util.Optional;

public abstract class SimpleLibConst<E extends Expression> implements LibConst {

  protected abstract E expressionFromString(String v);
  protected abstract String defaultStringValue();
  protected abstract Optional<String> validate(Expression expression);

  @Override
  public final Optional<E> edit(Node node, Expression oldValue) {
    return validate(oldValue)
      .or(() -> Optional.of(defaultStringValue()))
      .flatMap(v -> {
        var field = new TextField(v);
        var content = GridPanes.twoColumnPane(new SeqMap<>(text(name()), field));
        return new SimpleModalDialog<>(text(name()), node, content, field::getText).showAndWait();
      })
      .map(this::expressionFromString);
  }

  @Override
  public final Expression defaultValue() {
    return expressionFromString(defaultStringValue());
  }
}
