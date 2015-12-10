### Description ###
This implements RLE compressed bitset for Java. The RLE compression scheme is Word-Aligned Hybrid compression scheme from LBNL.

This package exists because of general lack of compressed bitset implementations in Java. This package implements the [Word-Aligned Hybrid](http://repositories.cdlib.org/cgi/viewcontent.cgi?article=3104&context=lbnl) (WAH) compression scheme from LBNL, which compresses the bitsets without sacrificing performance. The code is derived from the original authors' [FastBit](http://sdm.lbl.gov/fastbit/) software and pseudo-codes in the paper.

### Limitations ###
Currently the WAHBitSet class allows appends only and the number of operations implemented on the bitsets are also limited to AND, OR operations.

### Performance Note ###
In the compressed bitset, checking for every single bit is expensive, but doing operations such as AND and OR with other compressed bitsets is quite fast (They work on compressed bitsets, without uncompressing them at all). Use -server option while starting Java to get the most performance.