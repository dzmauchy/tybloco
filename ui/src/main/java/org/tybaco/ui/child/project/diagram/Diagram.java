package org.tybaco.ui.child.project.diagram;

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

import javafx.beans.Observable;
import javafx.collections.*;
import org.springframework.stereotype.Component;
import org.tybaco.editors.change.AddListChange;
import org.tybaco.editors.change.SetChange;
import org.tybaco.ui.child.project.classpath.BlockCache;
import org.tybaco.ui.child.project.classpath.ProjectClasspath;
import org.tybaco.ui.model.*;

@Component
public class Diagram extends AbstractDiagram {

  public final Project project;
  final BlockCache blockCache;
  final ProjectClasspath classpath;
  private final ListChangeListener<Block> blocksListener = this::onBlocksChange;
  private final SetChangeListener<Link> linkListener = this::onLinkChange;

  public Diagram(Project project, BlockCache blockCache, ProjectClasspath classpath) {
    this.project = project;
    this.blockCache = blockCache;
    this.classpath = classpath;
    blocksListener.onChanged(new AddListChange<>(project.blocks, 0, project.blocks.size()));
    project.links.forEach(l -> linkListener.onChanged(new SetChange<>(project.links, null, l)));
    initialize();
  }

  private void initialize() {
    project.blocks.addListener(new WeakListChangeListener<>(blocksListener));
    project.links.addListener(new WeakSetChangeListener<>(linkListener));
    blockCache.addListener(this::onClassPathChange);
  }

  private void onBlocksChange(ListChangeListener.Change<? extends Block> change) {
    while (change.next()) {
      if (change.wasRemoved()) {
        for (var removed : change.getRemoved()) {
          blocks.getChildren().removeIf(e -> e instanceof DiagramBlock b && b.block == removed);
        }
      }
      if (change.wasAdded()) {
        for (var added : change.getAddedSubList()) {
          blocks.getChildren().add(new DiagramBlock(this, added));
        }
      }
    }
  }

  private void onLinkChange(SetChangeListener.Change<? extends Link> change) {
    var e = change.wasAdded() ? change.getElementAdded() : change.getElementRemoved();
    for (var n : blocks.getChildren()) {
      if (n instanceof DiagramBlock b) {
        if (b.block.id == e.in.blockId || b.block.id == e.out.blockId) {
          b.onLink(e, change.wasAdded());
        }
      }
    }
    if (change.wasAdded()) {
      connectors.getChildren().add(new DiagramLine(change.getElementAdded()));
    } else {
      connectors.getChildren().removeIf(n -> n instanceof DiagramLine l && l.link == e);
    }
  }

  private void onClassPathChange(Observable o) {
    for (var block : blocks.getChildren()) if (block instanceof DiagramBlock b) b.onClasspathChange();
    project.links.forEach(l -> linkListener.onChanged(new SetChange<>(project.links, null, l)));
  }
}