/**
 *
 */
package org.mastodon;

import java.util.Arrays;
import java.util.Map;

import org.janelia.saalfeldlab.n5.Compression;
import org.janelia.saalfeldlab.n5.N5Reader;
import org.janelia.saalfeldlab.n5.imglib2.N5Utils;
import org.janelia.saalfeldlab.n5.universe.N5Factory;

import bdv.cache.SharedQueue;
import bdv.util.BdvFunctions;
import bdv.util.volatiles.VolatileViews;
import net.imglib2.cache.img.CachedCellImg;

/**
 * @author Stephan Saalfeld &lt;saalfelds@janelia.hhmi.org&gt;
 *
 */
public class N5Tutorial {

	public static void main(final String... args) {

//		final String containerUrl = "/home/saalfeld/tmp/jrc_hela-2.n5";
		final String containerUrl = "https://janelia-cosem.s3.amazonaws.com/jrc_hela-2/jrc_hela-2.n5";

		final N5Reader n5 = new N5Factory()
				.cacheAttributes(true)
				.hdf5DefaultBlockSize(64)
				.zarrDimensionSeparator("/")
				.zarrMapN5Attributes(true).openReader(containerUrl);

		/* list groups and datasets */
		final String[] groups = n5.list("/em/fibsem-uint16");

		/* test if something is a dataset (or a group) */
		final boolean test = n5.datasetExists("/em/fibsem-uint16/s4");

		/* dealing with attribute4s */
		final long[] attribute = n5.getAttribute("/em/fibsem-uint16/s4", "dimensions", long[].class);
		System.out.println(Arrays.toString(attribute));

		/* dealing with attribute4s */
		final String attribute2 = n5.getAttribute("/em/fibsem-uint16/s4", "compression/type", String.class);
		System.out.println(attribute2);

		final Compression attribute3 = n5.getAttribute("/em/fibsem-uint16/s4", "compression", Compression.class);
		System.out.println(attribute3);

		final Map<String, Class<?>> map = n5.listAttributes("/em/fibsem-uint16/s4");
		System.out.println(map);

		final CachedCellImg<?, ?> dataset = N5Utils.open(n5, "/em/fibsem-uint16/s4");
		System.out.println(dataset);

		//BdvFunctions.show(dataset, "some name");

		final CachedCellImg<?, ?> volatileRai = N5Utils.openVolatile(n5, "/em/fibsem-uint16/s4");
		final SharedQueue queue = new SharedQueue(Math.max(1, Runtime.getRuntime().availableProcessors() / 2));
		BdvFunctions.show(VolatileViews.wrapAsVolatile(volatileRai, queue), "volatile name");
	}

}
