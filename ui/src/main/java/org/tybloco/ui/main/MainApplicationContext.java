package org.tybloco.ui.main;

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

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;
import org.tybloco.ui.Main;
import org.tybloco.ui.lib.context.Propagated;
import org.tybloco.ui.lib.logging.LogBeanPostProcessor;
import org.tybloco.ui.splash.SplashBeanPostProcessor;

import static org.tybloco.logging.Log.info;

public final class MainApplicationContext extends AnnotationConfigApplicationContext {

  public MainApplicationContext() {
    setId("root");
    setDisplayName("Tybloco IDE");
    setClassLoader(Thread.currentThread().getContextClassLoader());
    setAllowCircularReferences(false);
    setAllowBeanDefinitionOverriding(false);
    Main.updateSplash.run();
  }

  @Override
  protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.addBeanPostProcessor(new SplashBeanPostProcessor());
    beanFactory.addBeanPostProcessor(new LogBeanPostProcessor("root"));
    super.prepareBeanFactory(beanFactory);
  }

  @Override
  protected void publishEvent(@Nonnull Object event, ResolvableType typeHint) {
    info(getClass(), "{0}", event);
    super.publishEvent(event, typeHint);
  }

  @Override
  protected void onRefresh() throws BeansException {
    Propagated.installErrorHandler(this);
  }
}
