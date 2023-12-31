package org.tybloco.xml;

/*-
 * #%L
 * library
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

import org.xml.sax.*;

public final class XmlErrorHandler implements ErrorHandler {

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    throw exception;
  }

  @Override
  public void error(SAXParseException exception) throws SAXException {
    if (exception.getMessage().contains("xsi:noNamespaceSchemaLocation")) {
      return;
    }
    throw exception;
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    throw exception;
  }
}
