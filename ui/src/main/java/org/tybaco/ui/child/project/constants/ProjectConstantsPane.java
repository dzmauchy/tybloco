package org.tybaco.ui.child.project.constants;

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

import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tybaco.editors.action.Action;

import java.util.List;

import static org.tybaco.editors.control.Toolbars.toolbar;

@Order(1)
@Qualifier("forProjectAccordion")
@Component
public class ProjectConstantsPane extends BorderPane {

  public ProjectConstantsPane(ProjectConstantsTable constantsTable, @Qualifier("constantListAction") List<Action> constantListActions) {
    super(constantsTable, toolbar(constantListActions), null, null, null);
    setId("Constants");
  }
}
