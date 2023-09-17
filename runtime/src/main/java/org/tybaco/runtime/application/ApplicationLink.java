package org.tybaco.runtime.application;

/*-
 * #%L
 * runtime
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

import org.w3c.dom.Element;

import java.util.NoSuchElementException;

import static org.tybaco.runtime.util.Xml.elementByTag;

public record ApplicationLink(ApplicationConnector out, ApplicationConnector in) {

  public ApplicationLink(Element element) {
    this(
      elementByTag(element, "out").map(ApplicationConnector::new).orElseThrow(() -> new NoSuchElementException("out")),
      elementByTag(element, "in").map(ApplicationConnector::new).orElseThrow(() -> new NoSuchElementException("in"))
    );
  }
}
