package net.trackmate.revised.model.mamut;

import net.trackmate.graph.AbstractEdgePool;
import net.trackmate.graph.AbstractVertexWithFeaturesPool;
import net.trackmate.graph.zzgraphinterfaces.GraphFeatures;
import net.trackmate.graph.zzgraphinterfaces.GraphIdBimap;
import net.trackmate.pool.ByteMappedElement;
import net.trackmate.pool.ByteMappedElementArray;
import net.trackmate.pool.MemPool;
import net.trackmate.pool.PoolObject;
import net.trackmate.pool.SingleArrayMemPool;
import net.trackmate.revised.model.AbstractModelGraph;


public // TODO: remove, temporarily made public for designing Features framework
class ModelGraph extends AbstractModelGraph< ModelGraph.SpotPool, ModelGraph.LinkPool, Spot, Link, ByteMappedElement >
{
	public ModelGraph()
	{
		this( 1000 );
	}

	public ModelGraph( final int initialCapacity )
	{
		super( new LinkPool( initialCapacity, new SpotPool( initialCapacity ) ) );
	}

	GraphFeatures< Spot, Link > features()
	{
		return features;
	}

	GraphIdBimap< Spot, Link > idmap()
	{
		return idmap;
	}

	static class SpotPool extends AbstractVertexWithFeaturesPool< Spot, Link, ByteMappedElement >
	{
		SpotPool( final int initialCapacity )
		{
			this( initialCapacity, new SpotFactory() );
		}

		private SpotPool( final int initialCapacity, final SpotFactory f )
		{
			super( initialCapacity, f );
			f.vertexPool = this;
		}

		private static class SpotFactory implements PoolObject.Factory< Spot, ByteMappedElement >
		{
			private SpotPool vertexPool;

			@Override
			public int getSizeInBytes()
			{
				return Spot.SIZE_IN_BYTES;
			}

			@Override
			public Spot createEmptyRef()
			{
				return new Spot( vertexPool );
			}

			@Override
			public MemPool.Factory< ByteMappedElement > getMemPoolFactory()
			{
				return SingleArrayMemPool.factory( ByteMappedElementArray.factory );
			}
		};
	}

	static class LinkPool extends AbstractEdgePool< Link, Spot, ByteMappedElement >
	{
		LinkPool( final int initialCapacity, final SpotPool vertexPool )
		{
			this( initialCapacity, new LinkFactory(), vertexPool );
		}

		private LinkPool( final int initialCapacity, final LinkPool.LinkFactory f, final SpotPool vertexPool )
		{
			super( initialCapacity, f, vertexPool );
			f.edgePool = this;
		}

		private static class LinkFactory implements PoolObject.Factory< Link, ByteMappedElement >
		{
			private LinkPool edgePool;

			@Override
			public int getSizeInBytes()
			{
				return Link.SIZE_IN_BYTES;
			}

			@Override
			public Link createEmptyRef()
			{
				return new Link( edgePool );
			}

			@Override
			public MemPool.Factory< ByteMappedElement > getMemPoolFactory()
			{
				return SingleArrayMemPool.factory( ByteMappedElementArray.factory );
			}
		};
	}

	/**
	 * TODO: This should be removed! It is currently only needed to construct
	 * SpatioTemporalIndexImp, which in turn needs it for KDTree. This should
	 * all be rather implemented using RefCollection and IdBimap.
	 */
	SpotPool getVertexPool()
	{
		return vertexPool;
	}

	@Override
	protected void notifyGraphChanged()
	{
		super.notifyGraphChanged();
	}
}
