package org.mastodon.ui.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * Maintain a list of the Nth most recent project paths manipulated with this
 * object.
 */
public class RecentProjects implements Iterable< String >
{

	private static final String RECENT_PROJECTS_FILE = System.getProperty( "user.home" ) + "/.mastodon/recentprojects.yaml";

	private static final int MAX_N_RECENT_PROJECTS = 10;

	private final List< String > recent;

	private final int maxLength;

	public RecentProjects()
	{
		this.recent = new ArrayList< String >();
		this.maxLength = MAX_N_RECENT_PROJECTS;
		load( RECENT_PROJECTS_FILE );
	}

	public void add( final String element )
	{
		recent.remove( element );
		recent.add( 0, element );
		reduce();
		save( RECENT_PROJECTS_FILE );
	}

	private void reduce()
	{
		while ( recent.size() > maxLength )
			recent.remove( recent.size() - 1 );
	}

	public void clear()
	{
		recent.clear();
		save( RECENT_PROJECTS_FILE );
	}

	@Override
	public Iterator< String > iterator()
	{
		return Collections.unmodifiableCollection( recent ).iterator();
	}

	public boolean isempty()
	{
		return recent.isEmpty();
	}

	public int size()
	{
		return recent.size();
	}

	public boolean remove( final String element )
	{
		final boolean removed = recent.remove( element );
		save( RECENT_PROJECTS_FILE );
		return removed;
	}

	@Override
	public String toString()
	{
		return recent.toString();
	}

	private static Yaml createYaml()
	{
		final DumperOptions dumperOptions = new DumperOptions();
		final Yaml yaml = new Yaml( dumperOptions );
		return yaml;
	}

	private void save( final String filename )
	{
		try (final FileWriter output = new FileWriter( filename ))
		{
			final Yaml yaml = createYaml();
			yaml.dumpAll( recent.iterator(), output );
		}
		catch ( final IOException e )
		{}
	}

	private void load( final String filename )
	{
		recent.clear();
		try (final FileReader input = new FileReader( filename ))
		{
			final Yaml yaml = createYaml();
			final Iterable< Object > objs = yaml.loadAll( input );
			for ( final Object obj : objs )
			{
				if ( obj instanceof String )
					recent.add( ( String ) obj );
			}
		}
		catch ( final FileNotFoundException e )
		{}
		catch ( final IOException e )
		{}
	}
}
