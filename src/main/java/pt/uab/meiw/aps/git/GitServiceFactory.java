package pt.uab.meiw.aps.git;

import pt.uab.meiw.aps.Service;
import pt.uab.meiw.aps.ServiceFactory;
import pt.uab.meiw.aps.git.impl.GitServiceImpl;

/**
 * The Git Service Factory.
 *
 * @author Hugo Gonçalves
 * @since 0.0.1
 */
public final class GitServiceFactory implements ServiceFactory {

  @Override
  public Service create() {
    return new GitServiceImpl();
  }
}
