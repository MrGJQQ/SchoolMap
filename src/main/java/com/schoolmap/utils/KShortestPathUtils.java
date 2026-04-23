package com.schoolmap.utils;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import com.schoolmap.entity.dto.PathPlanResult;
import com.schoolmap.entity.dto.SinglePathResult;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class KShortestPathUtils {

    private static final int K = 3;

    // ... existing code ...

    public static PathPlanResult findKShortestPaths(
            List<RoadNode> allNodes,
            List<RoadEdge> allEdges,
            String startNodeNo,
            String endNodeNo,
            Integer travelMode) {

        GraphUtils.RoadGraph graph = buildGraph(allNodes, allEdges, travelMode);

        GraphUtils.GraphNode startNode = graph.getNode(startNodeNo);
        GraphUtils.GraphNode endNode = graph.getNode(endNodeNo);

        if (startNode == null) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("起始节点不存在")
                    .build();
        }

        if (endNode == null) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("终止节点不存在")
                    .build();
        }

        List<Path> kShortestPaths = yenKShortestPath(graph, startNodeNo, endNodeNo, K);

        if (kShortestPaths.isEmpty()) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("未找到可行路径")
                    .build();
        }

        List<SinglePathResult> results = new ArrayList<>();
        String modeName = getTravelModeName(travelMode);

        for (int i = 0; i < kShortestPaths.size(); i++) {
            Path path = kShortestPaths.get(i);
            SinglePathResult singleResult = convertToSinglePathResult(path, graph, i + 1, modeName, allNodes, allEdges);
            results.add(singleResult);
        }

        String message = String.format("找到%d条%s路径", results.size(), modeName);

        return PathPlanResult.builder()
                .success(true)
                .message(message)
                .paths(results)
                .totalPaths(results.size())
                .build();
    }

// ... existing code ...


    private static List<Path> yenKShortestPath(GraphUtils.RoadGraph graph, String source, String sink, int k) {
        List<Path> A = new ArrayList<>();
PriorityQueue<Path> B = new PriorityQueue<>(Comparator.comparingDouble(Path::getTotalWeight));

        Path shortestPath = dijkstra(graph, source, sink, Collections.emptySet(), Collections.emptyMap());
        if (shortestPath == null) {
            return A;
        }
        A.add(shortestPath);

        for (int i = 1; i < k; i++) {
            if (A.size() < i) {
                break;
            }

            Path previousPath = A.get(i - 1);
            List<String> previousNodes = previousPath.getNodes();

            for (int j = 0; j < previousNodes.size() - 1; j++) {
                String spurNode = previousNodes.get(j);
                List<String> rootPathNodes = previousNodes.subList(0, j + 1);

                Set<String> removedEdges = new HashSet<>();
                for (Path path : A) {
                    List<String> pathNodes = path.getNodes();
                    if (pathNodes.size() > j && pathNodes.subList(0, j + 1).equals(rootPathNodes)) {
                        String edgeKey = pathNodes.get(j) + "->" + pathNodes.get(j + 1);
                        removedEdges.add(edgeKey);
                    }
                }

                Map<String, Set<String>> removedNodes = new HashMap<>();
                for (String node : rootPathNodes.subList(0, rootPathNodes.size() - 1)) {
                    removedNodes.put(node, new HashSet<>());
                }

                Path spurPath = dijkstra(graph, spurNode, sink, removedEdges, removedNodes);

                if (spurPath != null) {
                    List<String> totalPath = new ArrayList<>(rootPathNodes.subList(0, rootPathNodes.size() - 1));
                    totalPath.addAll(spurPath.getNodes());

                    double totalWeight = calculatePathWeight(graph, totalPath);

                    Path totalPathObj = new Path(totalPath, totalWeight);

                    boolean isDuplicate = false;
                    for (Path existingPath : B) {
                        if (existingPath.getNodes().equals(totalPathObj.getNodes())) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    for (Path existingPath : A) {
                        if (existingPath.getNodes().equals(totalPathObj.getNodes())) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (!isDuplicate) {
                        B.offer(totalPathObj);
                    }
                }
            }

            if (B.isEmpty()) {
                break;
            }

            Path nextPath = B.poll();
            A.add(nextPath);
        }

        return A;
    }

    private static Path dijkstra(GraphUtils.RoadGraph graph, String source, String sink,
                                  Set<String> removedEdges, Map<String, Set<String>> removedNodes) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        Set<String> visited = new HashSet<>();

        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(NodeDistance::getDistance));

        for (String nodeNo : graph.getNodes().keySet()) {
            distances.put(nodeNo, Double.MAX_VALUE);
        }
        distances.put(source, 0.0);

        queue.offer(new NodeDistance(source, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            String currentNodeNo = current.getNodeNo();

            if (visited.contains(currentNodeNo)) {
                continue;
            }
            visited.add(currentNodeNo);

            if (currentNodeNo.equals(sink)) {
                break;
            }

            GraphUtils.GraphNode node = graph.getNode(currentNodeNo);
            if (node == null || node.getEdges() == null) {
                continue;
            }

            for (GraphUtils.GraphEdge edge : node.getEdges()) {
                String neighborNo = edge.getEndNode();

                String edgeKey = currentNodeNo + "->" + neighborNo;
                if (removedEdges.contains(edgeKey)) {
                    continue;
                }

                if (removedNodes.containsKey(currentNodeNo) &&
                    removedNodes.get(currentNodeNo).contains(neighborNo)) {
                    continue;
                }

                if (visited.contains(neighborNo)) {
                    continue;
                }

                double newDistance = distances.get(currentNodeNo) + edge.getWeight();

                if (newDistance < distances.get(neighborNo)) {
                    distances.put(neighborNo, newDistance);
                    previous.put(neighborNo, currentNodeNo);
                    queue.offer(new NodeDistance(neighborNo, newDistance));
                }
            }
        }

        if (distances.get(sink) == Double.MAX_VALUE) {
            return null;
        }

        List<String> pathNodes = reconstructPath(previous, source, sink);
        return new Path(pathNodes, distances.get(sink));
    }

    private static List<String> reconstructPath(Map<String, String> previous, String source, String sink) {
        List<String> path = new ArrayList<>();
        String currentNode = sink;

        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = previous.get(currentNode);
        }

        if (!path.get(0).equals(source)) {
            return Collections.emptyList();
        }

        return path;
    }

    private static double calculatePathWeight(GraphUtils.RoadGraph graph, List<String> pathNodes) {
        double totalWeight = 0.0;
        for (int i = 0; i < pathNodes.size() - 1; i++) {
            String fromNode = pathNodes.get(i);
            String toNode = pathNodes.get(i + 1);

            GraphUtils.GraphNode node = graph.getNode(fromNode);
            if (node != null) {
                for (GraphUtils.GraphEdge edge : node.getEdges()) {
                    if (edge.getEndNode().equals(toNode)) {
                        totalWeight += edge.getWeight();
                        break;
                    }
                }
            }
        }
        return totalWeight;
    }

// ... existing code ...

    private static GraphUtils.RoadGraph buildGraph(List<RoadNode> allNodes, List<RoadEdge> allEdges, Integer travelMode) {
        GraphUtils.RoadGraph graph = new GraphUtils.RoadGraph();

        for (RoadNode node : allNodes) {
            graph.addNode(new GraphUtils.GraphNode(node));
        }

        for (RoadEdge edge : allEdges) {
            GraphUtils.GraphEdge graphEdge = new GraphUtils.GraphEdge(edge, travelMode);
            if (graphEdge.getWeight() != Double.MAX_VALUE) {
                graph.addEdge(graphEdge);
            }
        }

        return graph;
    }

    private static SinglePathResult convertToSinglePathResult(Path path, GraphUtils.RoadGraph graph,
                                                               int pathIndex, String modeName,
                                                               List<RoadNode> allNodes, List<RoadEdge> allEdges) {
        List<RoadNode> pathNodes = new ArrayList<>();
        List<RoadEdge> pathEdges = new ArrayList<>();

        List<String> nodeNos = path.getNodes();

        Map<String, RoadNode> nodeMap = new HashMap<>();
        for (RoadNode node : allNodes) {
            nodeMap.put(node.getNodeNo(), node);
        }

        Map<String, RoadEdge> edgeMap = new HashMap<>();
        for (RoadEdge edge : allEdges) {
            String edgeKey = edge.getBeginNode() + "-" + edge.getEndNode();
            if (!edgeMap.containsKey(edgeKey)) {
                edgeMap.put(edgeKey, edge);
            }
        }

        for (String nodeNo : nodeNos) {
            RoadNode node = nodeMap.get(nodeNo);
            if (node != null) {
                pathNodes.add(node);
            }
        }

        for (int i = 0; i < nodeNos.size() - 1; i++) {
            String fromNode = nodeNos.get(i);
            String toNode = nodeNos.get(i + 1);
            String edgeKey = fromNode + "-" + toNode;
            RoadEdge edge = edgeMap.get(edgeKey);
            if (edge != null) {
                pathEdges.add(edge);
            }
        }

        String description;
        if (pathIndex == 1) {
            description = String.format("最短路径（推荐）- %.2f米", path.getTotalWeight());
        } else if (pathIndex == 2) {
            description = String.format("次短路径 - %.2f米", path.getTotalWeight());
        } else {
            description = String.format("第三路径 - %.2f米", path.getTotalWeight());
        }

        return SinglePathResult.builder()
                .pathIndex(pathIndex)
                .pathNodes(pathNodes)
                .pathEdges(pathEdges)
                .totalDistance(path.getTotalWeight())
                .distanceUnit("米")
                .description(description)
                .build();
    }

// ... existing code ...


    private static String getTravelModeName(Integer travelMode) {
        if (travelMode == null) {
            return "未知";
        }
        switch (travelMode) {
            case 0:
                return "驾车";
            case 1:
                return "骑行";
            case 2:
                return "步行";
            default:
                return "未知";
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    private static class NodeDistance {
        private String nodeNo;
        private double distance;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    private static class Path {
        private List<String> nodes;
        private double totalWeight;
    }
}