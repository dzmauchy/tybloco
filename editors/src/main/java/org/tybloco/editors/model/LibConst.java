package org.tybloco.editors.model;

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
import org.tybloco.editors.Meta;
import org.tybloco.editors.text.TextSupport;

import java.util.Optional;

public interface LibConst extends Meta, TextSupport {
  Optional<? extends Expression> edit(Node node, Expression oldValue);
  String type();
  Expression defaultValue();
  String shortText(Expression expression);
}
