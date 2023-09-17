package org.tybaco.runtime.application.tasks;

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

import org.tybaco.runtime.application.Application;
import org.xml.sax.InputSource;

import java.net.URI;

import static org.tybaco.runtime.util.Xml.load;

public class ApplicationLoader implements ApplicationTask {

  private final String[] args;

  public ApplicationLoader(String[] args) {
    this.args = args;
  }

  @Override
  public void run(ApplicationContext context) throws Exception {
    context.application = application();
  }

  private Application application() throws Exception {
    if (args.length > 0) {
      var url = new URI(args[0]).toURL();
      return load(url, Application.schema(), Application::new);
    } else {
      var inputSource = new InputSource(System.in);
      inputSource.setEncoding("UTF-8");
      inputSource.setPublicId("stdin");
      return load(inputSource, Application.schema(), Application::new);
    }
  }
}