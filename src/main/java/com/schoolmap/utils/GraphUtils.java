package com.schoolmap.utils;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import lombok.Data;

import java.util.*;

public class GraphUtils {

    @Data
    public static class GraphNode {
        private String nodeNo;
        private String nodeName;
        private String gis;
        private Integer nodeType;
        private List<GraphEdge> edges;

        public GraphNode(RoadNode roadNode) {
            this.nodeNo = roadNode.getNodeNo();
            this.nodeName = roadNode.getNodeName();
            this.gis = roadNode.getGis();
            this.nodeType = roadNode.getNodeType();
            this.edges = new ArrayList<>();
        }
    }

    @Data
    public static class GraphEdge {
        private String roadNo;
        private String roadName;
        private String beginNode;
        private String endNode;
        private Double weight;
        private Integer roadType;

        public GraphEdge() {
        }

        public GraphEdge(RoadEdge roadEdge, Integer travelMode) {
            this.roadNo = roadEdge.getRoadNo();
            this.roadName = roadEdge.getRoadName();
            this.beginNode = roadEdge.getBeginNode();
            this.endNode = roadEdge.getEndNode();
            this.weight = calculateWeight(roadEdge, travelMode);
            this.roadType = roadEdge.getRoadType();
        }

        private Double calculateWeight(RoadEdge roadEdge, Integer travelMode) {
            try {
                double length = Double.parseDouble(roadEdge.getLength());

                switch (travelMode) {
                    case 0:
                        return calculateDrivingWeight(length, roadEdge.getRoadType());
                    case 1:
                        return calculateCyclingWeight(length, roadEdge.getRoadType());
                    case 2:
                        return calculateWalkingWeight(length, roadEdge.getRoadType());
                    default:
                        return length;
                }
            } catch (NumberFormatException e) {
                return Double.MAX_VALUE;
            }
        }

        private Double calculateDrivingWeight(double length, Integer roadType) {
            if (roadType == null || roadType != 0) {
                return Double.MAX_VALUE;
            }

            double speedFactor = getDrivingSpeedFactor(roadType);
            return length / speedFactor;
        }

        private Double calculateWalkingWeight(double length, Integer roadType) {
            if (roadType == null || roadType > 2) {
                return Double.MAX_VALUE;
            }

            double walkingSpeed = 5.0;
            return length / walkingSpeed;
        }

        private Double calculateCyclingWeight(double length, Integer roadType) {
            if (roadType == null || roadType > 1) {
                return Double.MAX_VALUE;
            }

            double speedFactor = getCyclingSpeedFactor(roadType);
            return length / speedFactor;
        }


        private double getDrivingSpeedFactor(Integer roadType) {
            switch (roadType) {
                case 1:
                    return 60.0;
                case 2:
                    return 40.0;
                default:
                    return 30.0;
            }
        }

        private double getCyclingSpeedFactor(Integer roadType) {
            switch (roadType) {
                case 1:
                    return 25.0;
                case 2:
                    return 20.0;
                default:
                    return 15.0;
            }
        }
    }

    @Data
    public static class RoadGraph {
        private Map<String, GraphNode> nodes;
        private List<GraphEdge> edges;

        public RoadGraph() {
            this.nodes = new HashMap<>();
            this.edges = new ArrayList<>();
        }

        public void addNode(GraphNode node) {
            nodes.put(node.getNodeNo(), node);
        }

        public void addEdge(GraphEdge edge) {
            edges.add(edge);

            GraphNode beginNode = nodes.get(edge.getBeginNode());
            if (beginNode != null) {
                beginNode.getEdges().add(edge);
            }

            GraphEdge reverseEdge = new GraphEdge();
            reverseEdge.setRoadNo(edge.getRoadNo());
            reverseEdge.setRoadName(edge.getRoadName());
            reverseEdge.setBeginNode(edge.getEndNode());
            reverseEdge.setEndNode(edge.getBeginNode());
            reverseEdge.setWeight(edge.getWeight());
            reverseEdge.setRoadType(edge.getRoadType());

            GraphNode endNode = nodes.get(edge.getEndNode());
            if (endNode != null) {
                endNode.getEdges().add(reverseEdge);
            }
        }

        public GraphNode getNode(String nodeNo) {
            return nodes.get(nodeNo);
        }
    }
}