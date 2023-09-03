package org.tybaco.ui.main.actions;

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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tybaco.ui.lib.action.Action;

@Component
public class ProjectActions {

  @Order(1)
  @Bean
  @Qualifier("projectMenu")
  public Action projectNewAction() {
    return new Action("New project", "MD_PROJECT", ev -> {

    });
  }

  @Order(2)
  @Bean
  @Qualifier("projectMenu")
  public Action projectNewActionSeparator() {
    return new Action();
  }

  @Order(3)
  @Bean
  @Qualifier("projectMenu")
  public Action saveAllProjectsAction() {
    return new Action("Save all projects", "MD_PROJECT", ev -> {

    });
  }
}
