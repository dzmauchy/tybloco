package org.tybloco.ui.model;

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
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static java.nio.ByteBuffer.allocate;
import static java.util.Base64.getUrlEncoder;
import static java.util.Collections.emptyList;
import static org.tybloco.xml.Xml.elementsByTag;
import static org.tybloco.xml.Xml.withChildren;

public final class Project {

  public final String id;
  public final ThreadGroup threadGroup;
  public final SimpleStringProperty name;
  public final ObservableList<Block> blocks;
  public final ObservableList<Constant> constants;
  public final SimpleSetProperty<Link> links;
  public final ObservableList<Dependency> dependencies;
  private final Observable[] observables;

  Project(String id, String name, Collection<Constant> constants, Collection<Block> blocks, Collection<Link> links, Collection<Dependency> dependencies) {
    this.id = id == null ? newId() : id;
    this.threadGroup = new ThreadGroup(id);
    this.name = new SimpleStringProperty(this, "name", name);
    this.constants = Constant.newList(constants);
    this.blocks = Block.newList(blocks);
    this.links = new SimpleSetProperty<>(this, "links", FXCollections.observableSet(links.toArray(Link[]::new)));
    this.dependencies = Dependency.libs(dependencies);
    this.observables = new Observable[] {this.name, this.constants, this.blocks, this.dependencies};
  }

  public Project(String name) {
    this(null, name, emptyList(), emptyList(), emptyList(), emptyList());
  }

  public Project(Element element) {
    this(
      element.getAttribute("id"),
      element.getAttribute("name"),
      elementsByTag(element, "constant").map(Constant::new).toList(),
      elementsByTag(element, "block").map(Block::new).toList(),
      elementsByTag(element, "link").map(Link::new).toList(),
      elementsByTag(element, "dependency").map(Dependency::new).toList()
    );
  }

  public void saveTo(Element element) {
    element.setAttribute("id", id);
    element.setAttribute("name", name.get());
    withChildren(element, "constant", constants, Constant::saveTo);
    withChildren(element, "block", blocks, Block::saveTo);
    withChildren(element, "link", links, Link::saveTo);
    withChildren(element, "dependency", dependencies, Dependency::saveTo);
  }

  private Observable[] observables() {
    return observables;
  }

  public static ObservableList<Project> newList() {
    return FXCollections.observableArrayList(Project::observables);
  }

  public Block newBlock(String name, String factoryId, double x, double y) {
    var block = new Block(nextId(), name, factoryId, x, y);
    blocks.add(block);
    return block;
  }

  public Constant newConstant(@NotNull String name, @NotNull String factoryId, @NotNull String value) {
    var constant = new Constant(nextId(), name, factoryId, value);
    constants.add(constant);
    return constant;
  }

  private int nextId() {
    var set = new BitSet();
    blocks.forEach(b -> set.set(b.id));
    constants.forEach(c -> set.set(c.id));
    return set.nextClearBit(0);
  }

  private String newId() {
    var hash = System.identityHashCode(this);
    var time = System.currentTimeMillis() - 1_600_000_000_000L;
    var longId = (time << 32) | ((long) hash & 0xFFFF_FFFFL);
    return getUrlEncoder().withoutPadding().encodeToString(allocate(8).putLong(0, longId).array());
  }

  public String guessConstantName() {
    return guessName(constants, c -> c.name.get(), "c");
  }

  public String guessBlockName() {
    return guessName(blocks, b -> b.name.get(), "b");
  }

  private static <E> String guessName(Collection<E> collection, Function<E, String> nameExtractor, String prefix) {
    return prefix + (collection.stream()
      .map(nameExtractor)
      .filter(s -> s.startsWith(prefix) && s.chars().skip(prefix.length()).allMatch(Character::isDigit))
      .map(v -> new BigInteger(v.substring(prefix.length())))
      .mapToInt(BigInteger::intValue)
      .max()
      .orElse(0) + 1);
  }

  @Override
  public String toString() {
    return name.get() + " (" + id + ")";
  }
}
