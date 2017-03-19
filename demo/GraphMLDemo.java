/*
 * (C) Copyright 2016-2017, by Dimitrios Michail and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * This program and the accompanying materials are dual-licensed under
 * either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation, or (at your option) any
 * later version.
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation.
 */
package demo;

import java.io.*;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.ext.GraphMLExporter.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;

/**
 * This class demonstrates exporting and importing a graph with custom vertex and edge attributes in
 * GraphML. Vertices of the graph have an attribute called "color" and a "name" attribute. Edges
 * have a "weight" attribute as well as a "name" attribute.
 * 
 * The demo constructs a complete graph with random edge weights and exports it as GraphML. The
 * output is then re-imported into a second graph which is exported a second time.
 */
public final class GraphMLDemo
{
    // Number of vertices
    private static final int SIZE = 6;

    private static Random generator = new Random(17);

    /**
     * Color
     */
    enum Color
    {
        BLACK("black"),
        WHITE("white");

        private final String value;

        private Color(String value)
        {
            this.value = value;
        }

        public String toString()
        {
            return value;
        }

    }

    /**
     * A custom graph vertex.
     */
    static class CustomVertex
    {
        private String id;
        private Color color;

        public CustomVertex(String id)
        {
            this(id, null);
        }

        public CustomVertex(String id, Color color)
        {
            this.id = id;
            this.color = color;
        }

        @Override
        public int hashCode()
        {
            return (id == null) ? 0 : id.hashCode();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CustomVertex other = (CustomVertex) obj;
            if (id == null) {
                return other.id == null;
            } else {
                return id.equals(other.id);
            }
        }

        public Color getColor()
        {
            return color;
        }

        public void setColor(Color color)
        {
            this.color = color;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        @Override
        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(id);
            if (color != null) {
                sb.append(",").append(color);
            }
            sb.append(")");
            return sb.toString();
        }
    }

    /**
     * Create exporter
     */
    private static GraphExporter<CustomVertex, DefaultWeightedEdge> createExporter()
    {
        // create GraphML exporter
        GraphMLExporter<CustomVertex, DefaultWeightedEdge> exporter =
            new GraphMLExporter<>(new ComponentNameProvider<CustomVertex>()
            {
                @Override
                public String getName(CustomVertex v)
                {
                    return v.id;
                }
            }, null, new IntegerComponentNameProvider<>(), null);

        // set to export the internal edge weights
        exporter.setExportEdgeWeights(true);

        // register additional color attribute for vertices
        exporter.registerAttribute("color", AttributeCategory.NODE, AttributeType.STRING);

        // register additional name attribute for vertices and edges
        exporter.registerAttribute("name", AttributeCategory.ALL, AttributeType.STRING);

        // create provider of vertex attributes
        ComponentAttributeProvider<CustomVertex> vertexAttributeProvider =
            new ComponentAttributeProvider<CustomVertex>()
            {
                @Override
                public Map<String, String> getComponentAttributes(CustomVertex v)
                {
                    Map<String, String> m = new HashMap<String, String>();
                    if (v.getColor() != null) {
                        m.put("color", v.getColor().toString());
                    }
                    m.put("name", "node-" + v.id);
                    return m;
                }
            };
        exporter.setVertexAttributeProvider(vertexAttributeProvider);

        // create provider of edge attributes
        ComponentAttributeProvider<DefaultWeightedEdge> edgeAttributeProvider =
            new ComponentAttributeProvider<DefaultWeightedEdge>()
            {
                @Override
                public Map<String, String> getComponentAttributes(DefaultWeightedEdge e)
                {
                    Map<String, String> m = new HashMap<String, String>();
                    m.put("name", e.toString());
                    return m;
                }
            };
        exporter.setEdgeAttributeProvider(edgeAttributeProvider);

        return exporter;
    }

    /**
     * Create importer
     */
    private static GraphImporter<CustomVertex, DefaultWeightedEdge> createImporter()
    {
        // create vertex provider
        VertexProvider<CustomVertex> vertexProvider = new VertexProvider<CustomVertex>()
        {
            @Override
            public CustomVertex buildVertex(String id, Map<String, String> attributes)
            {
                CustomVertex cv = new CustomVertex(id);

                // read color from attributes
                String color = attributes.get("color");
                if (color != null) {
                    switch (color) {
                    case "black":
                        cv.setColor(Color.BLACK);
                        break;
                    case "white":
                        cv.setColor(Color.WHITE);
                        break;
                    default:
                        // ignore not supported color
                    }
                }
                return cv;
            }
        };

        // create edge provider
        EdgeProvider<CustomVertex, DefaultWeightedEdge> edgeProvider =
            new EdgeProvider<CustomVertex, DefaultWeightedEdge>()
            {
                @Override
                public DefaultWeightedEdge buildEdge(
                    CustomVertex from, CustomVertex to, String label,
                    Map<String, String> attributes)
                {
                    return new DefaultWeightedEdge();
                }
            };

        // create GraphML importer
        GraphMLImporter<CustomVertex, DefaultWeightedEdge> importer =
            new GraphMLImporter<>(vertexProvider, edgeProvider);

        return importer;
    }

    /**
     * Main demo method
     * 
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        /*
         * Generate complete graph.
         * 
         * Vertices have random colors and edges have random edge weights.
         */
        DirectedPseudograph<CustomVertex, DefaultWeightedEdge> graph1 =
            new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
        CompleteGraphGenerator<CustomVertex, DefaultWeightedEdge> completeGenerator =
            new CompleteGraphGenerator<>(SIZE);
        VertexFactory<CustomVertex> vFactory = new VertexFactory<CustomVertex>()
        {
            private int id = 0;

            @Override
            public CustomVertex createVertex()
            {
                CustomVertex v = new CustomVertex(String.valueOf(id++));
                if (generator.nextBoolean()) {
                    v.setColor(Color.BLACK);
                } else {
                    v.setColor(Color.WHITE);
                }
                return v;
            }

        };
        System.out.println("-- Generating complete graph");
        completeGenerator.generateGraph(graph1, vFactory, null);

        // assign random weights
        for (DefaultWeightedEdge e : graph1.edgeSet()) {
            graph1.setEdgeWeight(e, generator.nextInt(100));
        }

        // now export and import back again
        try {
            // export as string
            System.out.println("-- Exporting graph as GraphML");
            GraphExporter<CustomVertex, DefaultWeightedEdge> exporter = createExporter();
            Writer writer = new StringWriter();
            exporter.exportGraph(graph1, writer);
            String graph1AsGraphML = writer.toString();

            // display
            System.out.println(graph1AsGraphML);

            // import it back
            System.out.println("-- Importing graph back from GraphML");
            Graph<CustomVertex, DefaultWeightedEdge> graph2 =
                new DirectedWeightedPseudograph<>(DefaultWeightedEdge.class);
            GraphImporter<CustomVertex, DefaultWeightedEdge> importer = createImporter();
            importer.importGraph(graph2, new StringReader(graph1AsGraphML));

        } catch (ExportException | ImportException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }

    }

}
