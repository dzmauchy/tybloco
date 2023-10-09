package org.tybaco.editors.basic.block.sink;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tybaco.editors.annotation.Input;
import org.tybaco.editors.annotation.Output;
import org.tybaco.editors.model.*;

import java.util.List;
import java.util.Map;

@Qualifier("basic")
@Component
@Descriptor(
  id = "BiSink",
  name = "Sink of key-value entries",
  icon = "remixal-dropbox-line",
  description = "Sink of key-value entries"
)
@Input(
  id = "threadFactory",
  name = "Thread factory",
  icon = "ri-5star-shadow",
  description = "A thread factory used to create the main thread of the sink",
  defaultValue = "$defaultThreadFactory"
)
@Input(
  id = "source",
  name = "Source",
  icon = "ri-dharma-wheel",
  description = "A key-value source"
)
@Input(
  id = "executorByKey",
  name = "Provider of key-specific executor",
  icon = "ri-colours",
  description = "A provider of executors for keys",
  defaultValue = "$defaultExecutorByKey"
)
@Input(
  id = "consumer",
  name = "Consumer",
  icon = "ri-react",
  description = "A consumer to consume the each key-value pair of the source"
)
@Input(
  id = "onError",
  name = "Error handler",
  icon = "ri-sdg",
  description = "Error handler used to handle each error",
  defaultValue = "$defaultErrorHandler"
)
@Output(
  id = "self",
  name = "This sink",
  icon = "mdal-alternate_email",
  description = "Resulting sink"
)
public final class BiSink implements LibBlock {

  @Override
  public BlockResult build(String var, Map<String, List<Expression>> inputs) {
    return null;
  }
}