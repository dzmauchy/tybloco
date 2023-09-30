package org.tybaco.editors.basic.list;

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

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.tybaco.editors.model.*;
import org.tybaco.editors.util.SeqMap;

import java.util.List;
import java.util.Map;

@Qualifier("basic")
@Descriptor(id = "list", name = "List", icon = "eva-list", description = "Creates a generic immutable list")
public final class ListBlock implements LibBlock {

  @Override
  public SeqMap<String, LibInput> inputs() {
    return new SeqMap<>(
      "elements", new LibInput("Elements", "mdi2l-list-status", "Variable number of arguments", true)
    );
  }

  @Override
  public SeqMap<String, LibOutput> outputs() {
    return new SeqMap<>(
      "self", new LibOutput("List of elements", "mdi2l-list-status", "Resulting list of elements")
    );
  }

  @Override
  public BlockResult build(Map<String, List<Expression>> inputs) {
    return new BlockResult(
      new MethodCallExpr(
        new ClassExpr(new ClassOrInterfaceType(null, "java.util.List")),
        "of",
        NodeList.nodeList()
      ),
      new SeqMap<>(
        "self", new ThisExpr()
      )
    );
  }
}
