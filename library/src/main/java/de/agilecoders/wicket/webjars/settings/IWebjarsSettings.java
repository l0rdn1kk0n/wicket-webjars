package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;

import java.util.regex.Pattern;

/**
 * Settings interface for all webjars depended settings
 *
 * @author miha
 */
public interface IWebjarsSettings {

    /**
     * @return {@link de.agilecoders.wicket.webjars.settings.ResourceStreamProvider} to use to load resources
     */
    ResourceStreamProvider resourceStreamProvider();

    /**
     * @return a set of {@link de.agilecoders.wicket.webjars.collectors.AssetPathCollector} instances to use to find
     * resources
     */
    AssetPathCollector[] assetPathCollectors();

    /**
     * @return the webjars package path (e.g. "META-INF.resources.webjars")
     */
    String webjarsPackage();

    /**
     * @return the path where all webjars are stored (e.g. "META-INF/resources/webjars")
     */
    String webjarsPath();

    /**
     * @return classloaders to use
     */
    ClassLoader[] classLoaders();

    /**
     * @return the pattern to filter accepted webjars resources
     */
    Pattern resourcePattern();
}