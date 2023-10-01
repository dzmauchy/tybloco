package org.tybaco.editors.basic.constant.numeric;

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
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tybaco.editors.dialog.ConstantEditDialog;
import org.tybaco.editors.model.*;
import org.tybaco.editors.util.SeqMap;
import org.tybaco.editors.value.StringValue;
import org.tybaco.editors.value.Value;

import java.util.Optional;

import static org.tybaco.editors.control.GridPanes.twoColumnPane;

@Qualifier("basic")
@Component
@Descriptor(id = "int", name = "Integer", icon = "mdi2n-numeric-0", description = "32 bit signed integer number")
public final class IntConstant implements SimpleLibBlock {

  @Override
  public Optional<Value> edit(Window window, Value old) {
    var field = new TextField(extractValue(old));
    return new ConstantEditDialog(this, window, twoColumnPane(new SeqMap<>(text("Number"), field)))
      .showAndWait(() -> new StringValue(field.getText()));
  }

  @Override
  public Expression build(Value value) {
    return new IntegerLiteralExpr(extractValue(value));
  }

  @Override
  public Value defaultValue() {
    return new StringValue("0");
  }
}