package org.tybaco.ui.child.project;

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

import org.kordamp.ikonli.materialdesign2.MaterialDesignB;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tybaco.ui.child.project.classpath.ProjectClasspath;
import org.tybaco.ui.child.project.constants.LibraryConstantsTree;
import org.tybaco.ui.lib.action.Action;
import org.tybaco.ui.model.Project;

import static org.tybaco.logging.Log.info;

@Component
public class ProjectActions {

  @Bean
  @Qualifier("projectAction")
  @Order(1)
  public Action newBlockAction(Project project, ProjectClasspath classpath) {
    return new Action(null, MaterialDesignB.BABY_BOTTLE, "New block", ev -> {
      var factory = "com.example.factory";
      var method = "method";
      var block = project.newBlock(project.guessBlockName(), factory, method, 0d, 0d);
      info(getClass(), "Block {0} created", block.id);
    }).separatorGroup("block").disabled(classpath.classPathIsNotSet);
  }

  @Bean
  @Qualifier("projectAction")
  @Order(2)
  public Action newConstantAction(ObjectProvider<LibraryConstantsTree.Win> win, ProjectClasspath classpath) {
    return new Action(null, MaterialDesignB.BULLSEYE, "New constant", ev -> {
      var window = win.getObject();
      window.show();
    }).separatorGroup("constant").disabled(classpath.classPathIsNotSet);
  }

  @Bean
  @Qualifier("projectAction")
  @Order(1001)
  public Action accordionVisibleAction(ProjectAccordion accordion) {
    return new Action(null, MaterialDesignB.BOOK_OPEN, "Accordion visibility")
      .selectionBoundTo(accordion.visibleProperty(), true)
      .separatorGroup("visibility");
  }
}
