package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.WebJarAssetLocator;
import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.util.IResourceStreamProvider;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Knows how to find webjars resources.
 *
 * @author miha
 */
public class WebjarsResourceFinder implements IResourceFinder {
    private static final Logger LOG = LoggerFactory.getLogger("wicket-webjars");

    private final WebJarAssetLocator locator;
    private final IResourceStreamProvider resourceStreamProvider;
    private final IWebjarsSettings settings;
    private final int hashCode;

    /**
     * Construct.
     *
     * @param settings the webjars settings to use
     */
    public WebjarsResourceFinder(IWebjarsSettings settings) {
        this.settings = settings;
        this.locator = newLocator();
        this.resourceStreamProvider = settings.resourceStreamProvider().newInstance(settings.classLoaders());

        int result = locator != null ? locator.hashCode() : 0;
        result = 31 * result + (settings != null ? settings.hashCode() : 0);
        this.hashCode = result;
    }

    /**
     * @return new resource locator instance
     */
    protected WebJarAssetLocator newLocator() {
        return new WebJarAssetLocator(settings);
    }

    /**
     * Looks for a given path name along the webjars root path
     *
     * @param clazz    The class requesting the resource stream
     * @param pathName The filename with possible path
     * @return The resource stream
     */
    @Override
    public IResourceStream find(final Class<?> clazz, final String pathName) {
        IResourceStream stream = null;

        if (IWebjarsResourceReference.class.isAssignableFrom(clazz)) {
            final int pos = pathName != null ? pathName.lastIndexOf("/webjars/") : -1;

            if (pos > -1) {
                try {
                    final String webjarsPath = locator.getFullPath(pathName.substring(pos));

                    LOG.debug("webjars path: {}", webjarsPath);

                    stream = newResourceStream(webjarsPath);
                } catch (Exception e) {
                    LOG.debug("can't locate resource for: {}; {}", pathName, e.getMessage(), e);
                }

                if (stream == null) {
                    LOG.debug("there is no webjars resource for: {}", pathName);
                }
            }

        }

        return stream;
    }

    /**
     * creates a new {@link IResourceStream} for given resource path with should be loaded by given
     * class loader.
     *
     * @param webjarsPath The resource to load
     * @return new {@link IResourceStream} instance that represents the content of given resource path or
     * null if resource wasn't found
     */
    protected IResourceStream newResourceStream(final String webjarsPath) {
        return resourceStreamProvider.newResourceStream(webjarsPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebjarsResourceFinder that = (WebjarsResourceFinder) o;

        if (locator != null ? !locator.equals(that.locator) : that.locator != null) {
            return false;
        }
        if (settings != null ? !settings.equals(that.settings) : that.settings != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
