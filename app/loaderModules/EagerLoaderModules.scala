package loaderModules

import com.google.inject.AbstractModule
import services.DatabaseTableCreator

/**
 * Creates(or executes) objects/singletons on a startup of the application.
 */
class EagerLoaderModules extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[DatabaseTableCreator]).asEagerSingleton()
  }
}
