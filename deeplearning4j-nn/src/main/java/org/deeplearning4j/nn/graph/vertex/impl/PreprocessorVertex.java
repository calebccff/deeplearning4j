/*-
 *
 *  * Copyright 2016 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.nn.graph.vertex.impl;

import org.deeplearning4j.nn.api.MaskState;
import org.deeplearning4j.nn.api.activations.Activations;
import org.deeplearning4j.nn.api.gradients.Gradients;
import org.deeplearning4j.nn.conf.InputPreProcessor;
import org.deeplearning4j.nn.graph.vertex.BaseGraphVertex;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.primitives.Pair;

/** PreprocessorVertex is a simple adaptor class that allows a {@link InputPreProcessor} to be used in a ComputationGraph
 * GraphVertex, without it being associated with a layer.
 * @author Alex Black
 */
public class PreprocessorVertex extends BaseGraphVertex {

    private InputPreProcessor preProcessor;

    public PreprocessorVertex(String name, int vertexIndex, int numInputs, InputPreProcessor preProcessor) {
        super(name, vertexIndex, numInputs);
        this.preProcessor = preProcessor;
    }

    @Override
    public Activations activate(boolean training) {
        return preProcessor.preProcess(input, getInputMiniBatchSize(), training);
    }

    @Override
    public Gradients backpropGradient(Gradients gradient) {
        return preProcessor.backprop(gradient, -1); //TODO
    }

    @Override
    public String toString() {
        return "PreprocessorVertex(id=" + this.getIndex() + ",name=\"" + this.getName() + "\",preProcessor="
                        + preProcessor.toString() + ")";
    }

    @Override
    public void setBackpropGradientsViewArray(INDArray backpropGradientsViewArray) {
        if (backpropGradientsViewArray != null)
            throw new RuntimeException("Vertex does not have gradients; gradients view array cannot be set here");
    }

    @Override
    public Pair<INDArray, MaskState> feedForwardMaskArrays(INDArray[] maskArrays, MaskState currentMaskState,
                    int minibatchSize) {
        //No op
        if (maskArrays == null || maskArrays.length == 0) {
            return null;
        }

//        return preProcessor.feedForwardMaskArray(maskArrays[0], currentMaskState, minibatchSize);
        throw new UnsupportedOperationException();
    }
}
