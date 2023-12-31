package org.tybloco.ui.child.project.constants;

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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.tybloco.editors.model.ConstLib;
import org.tybloco.editors.model.LibConst;
import org.tybloco.ui.child.project.classpath.Editors;
import org.tybloco.ui.child.project.classpath.ProjectClasspath;
import org.tybloco.ui.child.project.meta.MetaTree;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@Component
public final class LibraryConstantsTree extends MetaTree<ConstLib> {

  public LibraryConstantsTree(Editors editors, ProjectClasspath classpath) {
    super(editors.constLibs, classpath, m -> m instanceof LibConst);
  }
}
