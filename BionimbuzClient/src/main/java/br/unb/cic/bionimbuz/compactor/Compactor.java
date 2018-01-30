package br.unb.cic.bionimbuz.compactor;

/**
 * Interface of a Compactor implementation class.
 *
 * @author Vinicius
 */
public interface Compactor {

    /**
     * Compact a File
     *
     * @param inputFile
     * @return compressed File
     */
    public String compact(String inputFile);
}
