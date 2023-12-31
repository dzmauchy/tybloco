package org.tybloco.io;

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

import java.io.IOException;
import java.nio.file.*;

public interface Paths {

  static void deleteRecursively(Path path) throws IOException {
    if (Files.isDirectory(path)) {
      try {
        try (var ds = Files.newDirectoryStream(path)) {
          for (var file : ds) {
            deleteRecursively(file);
          }
        }
      } catch (NoSuchFileException | NotDirectoryException ignore) {
      }
    }
    Files.deleteIfExists(path);
  }
}
