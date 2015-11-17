package net.trackmate.revised.model.mamut;

import net.trackmate.revised.bdv.overlay.OverlayGraph;
import net.trackmate.revised.bdv.overlay.wrap.OverlayProperties;
import net.trackmate.revised.ui.selection.Selection;

/**
 * Provides spot {@link OverlayProperties properties} for BDV {@link OverlayGraph}.
 *
 * @author Tobias Pietzsch &lt;tobias.pietzsch@gmail.com&gt;
 */
public class ModelOverlayProperties implements OverlayProperties< Spot, Link >
{
	private final BoundingSphereRadiusStatistics radiusStats;

	private final Selection< Spot, Link > selection;

	public ModelOverlayProperties( final BoundingSphereRadiusStatistics radiusStats, final Selection< Spot, Link > selection )
	{
		this.radiusStats = radiusStats;
		this.selection = selection;
	}

	@Override
	public void localize( final Spot v, final float[] position )
	{
		v.localize( position );
	}

	@Override
	public void localize( final Spot v, final double[] position )
	{
		v.localize( position );
	}

	@Override
	public float getFloatPosition( final Spot v, final int d )
	{
		return v.getFloatPosition( d );
	}

	@Override
	public double getDoublePosition( final Spot v, final int d )
	{
		return v.getDoublePosition( d );
	}

	@Override
	public int numDimensions( final Spot v )
	{
		return v.numDimensions();
	}

	@Override
	public void getCovariance( final Spot v, final double[][] mat )
	{
		v.getCovariance( mat );
	}

	@Override
	public double getBoundingSphereRadiusSquared( final Spot v )
	{
		return v.getBoundingSphereRadiusSquared();
	}

	@Override
	public int getTimepoint( final Spot v )
	{
		return v.getTimepoint();
	}

	@Override
	public boolean isVertexSelected( final Spot v )
	{
		return selection.isSelected( v );
	}

	@Override
	public boolean isEdgeSelected( final Link e )
	{
		return selection.isSelected( e );
	}

	@Override
	public double getMaxBoundingSphereRadiusSquared( final int timepoint )
	{
		radiusStats.readLock().lock();
		try
		{
			return radiusStats.getMaxBoundingSphereRadiusSquared( timepoint );
		}
		finally
		{
			radiusStats.readLock().unlock();
		}
	}
}
