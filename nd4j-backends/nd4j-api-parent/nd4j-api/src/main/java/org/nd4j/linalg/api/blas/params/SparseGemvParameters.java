package org.nd4j.linalg.api.blas.params;

import lombok.Data;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.BaseSparseNDArrayCOO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ndarray.SparseFormat;
import org.nd4j.linalg.factory.Nd4j;

/**
 * @author Audrey Loeffel
 */
@Data
public class SparseGemvParameters {

    private int m, nnz;
    DataBuffer val, rowInd, colInd;
    private INDArray a, x, y;
    private char aOrdering = 'N';

    public SparseGemvParameters(INDArray a, INDArray x, INDArray y){
        this.a = a;
        this.x = x;
        this.y = y;

        if(a.isMatrix() && a.getFormat() == SparseFormat.COO){
            BaseSparseNDArrayCOO coo = (BaseSparseNDArrayCOO) a;
            val = coo.getIncludedValues();
            nnz = coo.nnz();
            m = coo.rows();
            setIndexes(coo);
        }
    }

    private void setIndexes(BaseSparseNDArrayCOO coo){
        int[] idx = coo.getIncludedIndices().asInt();
        int[] rows = new int[nnz];
        int[] cols = new int[nnz];
        for(int i = 0; i < nnz; i++){
            rows[i] = idx[i*2] + 1;
            cols[i] = idx[(i*2)+1] + 1;
        }
         rowInd = Nd4j.createBuffer(rows);
         colInd = Nd4j.createBuffer(cols);
    }

}
