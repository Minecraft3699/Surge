package com.mc3699.surge.base;

import com.mc3699.surge.Surge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public class Electron {
    private final BlockPos origin; // the source that the electron was started by
    private final LevelAccessor level;

    public Electron(BlockPos origin, LevelAccessor level) {
        this.origin = origin;
        this.level = level;
    }

    private static class CircuitTreeNode {
        public final BlockPos position;         // Position of this CircuitPart
        public final CircuitPart part;          // The CircuitPart at this node
        public final Map<BlockPos, CircuitTreeNode> children = new HashMap<>();

        public CircuitTreeNode(BlockPos position, CircuitPart part) {
            this.position = position;
            this.part = part;
        }
    }
    private static CircuitTreeNode buildCircuitTree(ElectronReturn result) {
        CircuitTreeNode root = null;
        Map<BlockPos, CircuitTreeNode> nodeRegistry = new HashMap<>();

        for (List<CircuitPart> path : result.allPaths) {
            if (path.isEmpty()) continue;

            // Initialize root if not set (first path's origin)
            BlockPos originPos = path.get(0).getBlockPos();
            if (root == null) {
                root = new CircuitTreeNode(originPos, path.get(0));
                nodeRegistry.put(originPos, root);
            }

            CircuitTreeNode current = root;
            for (int i = 1; i < path.size(); i++) {
                CircuitPart part = path.get(i);
                BlockPos pos = part.getBlockPos();

                // Check if this node already exists
                CircuitTreeNode child = nodeRegistry.get(pos);
                if (child == null) {
                    child = new CircuitTreeNode(pos, part);
                    nodeRegistry.put(pos, child);
                }

                // Link child to current node if not already linked
                current.children.putIfAbsent(pos, child);
                current = child;
            }
        }

        return root;
    }

    public static class ElectronReturn {
        public boolean completeCircuit = false;
        public List<List<CircuitPart>> allPaths = new ArrayList<>();
    }
    private static class PathState {
        BlockPos current;
        BlockPos last;
        List<CircuitPart> circuitParts;
        Set<BlockPos> visited;

        PathState(BlockPos current, BlockPos last, List<CircuitPart> parts, Set<BlockPos> visited) {
            this.current = current;
            this.last = last;
            this.circuitParts = new ArrayList<>(parts);
            this.visited = new HashSet<>(visited);
        }
    }

    /*
    public ElectronReturn flow(BlockPos startPos) {
        ElectronReturn ret = new ElectronReturn();

        BlockPos current = startPos;
        BlockPos last = origin;

        // checks to make sure the thing calling the function didn't make a mistake
        if (!(startPos instanceof ElectricalConductor)) {
            Surge.LOGGER.error("an electron was started at a position that is not a conductor");
            return null;
        }
        if (!(level.getBlockEntity(origin) instanceof ElectricalSourceBlockEntity)) {
            Surge.LOGGER.error("an electron was started by something that is not a source");
            return null;
        }

        ret.circuitParts.add((CircuitPart) level.getBlockEntity(origin));

        while (true) {
            for (Direction dir : Direction.values()) {
                BlockPos nextPos = current.relative(dir);
                if (nextPos.equals(last)) {
                    continue;
                }
                if (nextPos.equals(origin)) {
                    // the electron has returned to its origin, mark circuit as complete and stop
                    ret.completeCircuit = true;
                    return ret;
                }

                BlockEntity potentialCircuitPart = level.getBlockEntity(nextPos);
                if (potentialCircuitPart instanceof CircuitPart circuitPart) {
                    if (circuitPart instanceof ElectricalSourceBlockEntity sourceCircuitPart) {
                        if (sourceCircuitPart.getPolarity(dir.getOpposite()) == ElectricalPolarity.NEUTRAL) {
                            continue;
                        }
                    }
                    ret.circuitParts.add(circuitPart);
                    current = nextPos; // current = circuitPart.getBlockPos();
                    last = current;
                }
            }
            if (current.equals(last)) {
                return ret;
            }
        }
    }
     */
    public ElectronReturn flow(BlockPos startPos) {
        ElectronReturn ret = new ElectronReturn();

        // checks to make sure the thing calling the function didn't make a mistake
        if (!(startPos instanceof ElectricalConductor)) {
            Surge.LOGGER.error("an electron was started at a position that is not a conductor");
            return null;
        }
        if (!(level.getBlockEntity(origin) instanceof ElectricalSourceBlockEntity)) {
            Surge.LOGGER.error("an electron was started by something that is not a source");
            return null;
        }

        Queue<PathState> queue = new LinkedList<>();
        Set<BlockPos> initialVisited = new HashSet<>();
        initialVisited.add(origin);
        initialVisited.add(startPos);
        List<CircuitPart> initialParts = new ArrayList<>();
        initialParts.add((CircuitPart) level.getBlockEntity(origin));
        queue.add(new PathState(startPos, origin, initialParts, initialVisited));

        while (!queue.isEmpty()) {
            PathState state = queue.poll();

            for (Direction dir : Direction.values()) {
                BlockPos nextPos = state.current.relative(dir);

                // skip backtracking or visited positions in this path
                if (nextPos.equals(state.last) || state.visited.contains(nextPos)) continue;

                // check if the circuit is complete
                if (nextPos.equals(origin)) {
                    ret.completeCircuit = true;
                    // clone the path to avoid mutation and add to allPaths
                    List<CircuitPart> completedPath = new ArrayList<>(state.circuitParts);
                    ret.allPaths.add(completedPath);
                    continue;
                }

                BlockEntity be = level.getBlockEntity(nextPos);
                if (be instanceof CircuitPart part) {
                    if (part instanceof ElectricalSourceBlockEntity source) {
                        Direction sourceDir = dir.getOpposite();
                        if (source.getPolarity(sourceDir) == ElectricalPolarity.NEUTRAL) continue;
                    }

                    // clone state for the new path
                    List<CircuitPart> newParts = new ArrayList<>(state.circuitParts);
                    newParts.add(part);

                    Set<BlockPos> newVisited = new HashSet<>(state.visited);
                    newVisited.add(nextPos);

                    queue.add(new PathState(nextPos, state.current, newParts, newVisited));
                }
            }
        }

        return ret;
    }

    public static void executeFlow(ElectronReturn electronReturn) {
        if (!electronReturn.completeCircuit) {
            return;
        }
        CircuitTreeNode tree = buildCircuitTree(electronReturn);

        // TODO: implement
    }
}

