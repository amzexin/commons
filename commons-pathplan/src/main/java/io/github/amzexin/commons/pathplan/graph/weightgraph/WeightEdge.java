package io.github.amzexin.commons.pathplan.graph.weightgraph;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: edge
 *
 * @author Vincent
 * @date 2020-01-17 19:16
 */
@AllArgsConstructor
@Data
public class WeightEdge<N, W extends Comparable<W>> implements Comparable<WeightEdge<N, W>> {

    private N v;

    private N w;

    private W weight;

    /**
     * other vertex
     *
     * @param v
     * @return
     */
    public N other(N v) {
        if (v != this.v && v != this.w) {
            throw new IllegalArgumentException(String.format("edge not exist %s", v));
        }
        return v.equals(this.v) ? this.w : this.v;
    }

    @Override
    public int compareTo(WeightEdge<N, W> that) {
        return this.weight.compareTo(that.weight);
    }
}
