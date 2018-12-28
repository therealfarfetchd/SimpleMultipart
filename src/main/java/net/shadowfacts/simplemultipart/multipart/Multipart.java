package net.shadowfacts.simplemultipart.multipart;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateFactory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.LootTables;
import net.minecraft.world.loot.context.LootContext;
import net.shadowfacts.simplemultipart.SimpleMultipart;
import net.shadowfacts.simplemultipart.util.MultipartPlacementContext;

import java.util.List;
import java.util.Random;

/**
 * The base class for a multipart object.
 *
 * Analogous to {@link net.minecraft.block.Block}.
 *
 * @author shadowfacts
 * @since 0.1.0
 */
public abstract class Multipart {

	private StateFactory<Multipart, MultipartState> stateFactory;
	private MultipartState defaultState;

	private Identifier dropTableId;

	// TODO: Settings object?
	public Multipart() {
		StateFactory.Builder<Multipart, MultipartState> builder = new StateFactory.Builder<>(this);
		appendProperties(builder);
		stateFactory = builder.build(MultipartState::new);
		defaultState = stateFactory.getDefaultState();
	}

	/**
	 * Used to add additional properties to the state factory.
	 * @param builder The state factory builder.
	 */
	protected void appendProperties(StateFactory.Builder<Multipart, MultipartState> builder) {}

	/**
	 * @return The default state for this multipart.
	 */
	public MultipartState getDefaultState() {
		return defaultState;
	}

	/**
	 * Sets a new default state for this multipart.
	 * @param defaultState The new default state.
	 */
	public void setDefaultState(MultipartState defaultState) {
		this.defaultState = defaultState;
	}

	/**
	 * @return The state factory for this multipart.
	 */
	public StateFactory<Multipart, MultipartState> getStateFactory() {
		return stateFactory;
	}

	/**
	 * Used to determine the state that should be used when this multipart is placed in the world from the given context.
	 *
	 * For example: determine which half a slab should be or which side a facade should be on.
	 *
	 * @param context The context in which this part is being placed.
	 * @return The state to place.
	 */
	public MultipartState getPlacementState(MultipartPlacementContext context) {
		return getDefaultState();
	}

	/**
	 * Can be used to provide additional information to custom {@link net.shadowfacts.simplemultipart.client.MultipartBakedModel}.
	 * The returned state will be passed to {@link net.shadowfacts.simplemultipart.client.MultipartBakedModel#getMultipartQuads(MultipartView, Direction, Random)}.
	 *
	 * Can be overridden, should only be called via {@link MultipartState#getStateForRendering}
	 *
	 * @param state The normal state of this multipart.
	 * @param view The view of this multipart.
	 * @return The state that will be used for rendering.
	 */
	@Deprecated
	public MultipartState getStateForRendering(MultipartState state, MultipartView view) {
		return state;
	}

	/**
	 * Retrieves the bounding shape this multipart should have.
	 *
	 * Can be overridden, should only be called via {@link MultipartState#getBoundingShape}
	 *
	 * @param state The specific state for which to return the bounding shape.
	 * @param view The current view of the multipart.
	 *             Will be {@code null} when the default bounding shape is determined prior to the part being inserted
	 *             into the container.
	 * @return The bounding shape of this multipart in the given state.
	 */
	@Deprecated
	public abstract VoxelShape getBoundingShape(MultipartState state, /*@Nullable*/ MultipartView view);

	/**
	 * @return The loot table ID used for to determine the drops by the default {@link Multipart#getDroppedStacks(MultipartState, MultipartView, LootContext.Builder)} implementation.
	 */
	public Identifier getDropTableId() {
		if (dropTableId == null) {
			Identifier id = SimpleMultipart.MULTIPART.getId(this);
			dropTableId = new Identifier(id.getNamespace(), "multiparts/" + id.getPath());
		}
		return dropTableId;
	}

	/**
	 * Retrieves a list of stacks that should be dropped in the world when this part is broken via {@link net.shadowfacts.simplemultipart.container.MultipartContainer#breakPart(MultipartView)}.
	 *
	 * Can be overridden, should only be called via {@link MultipartState#getDroppedStacks)}
	 *
	 * @param state The state of this part.
	 * @param view The view of this part.
	 * @param builder The {@link LootContext} builder, used by the default loot table-based implementation.
	 * @return The list of stacks to drop.
	 */
	@Deprecated
	public List<ItemStack> getDroppedStacks(MultipartState state, MultipartView view, LootContext.Builder builder) {
		Identifier dropTableId = getDropTableId();
		if (dropTableId == LootTables.EMPTY) {
			return ImmutableList.of();
		} else {
			LootContext context = builder.put(SimpleMultipart.MULTIPART_STATE_PARAMETER, state).build(SimpleMultipart.MULTIPART_LOOT_CONTEXT);
			ServerWorld world = context.getWorld();
			LootSupplier supplier = world.getServer().getLootManager().getSupplier(dropTableId);
			return supplier.getDrops(context);
		}
	}

	/**
	 * Called when this part is activated (i.e. right-clicked) in the world.
	 *
	 * Can be overridden, should only be called via {@link MultipartState#activate}
	 *
	 * @param state The state of this part.
	 * @param view The view of this part.
	 * @param player The player that activated this part.
	 * @param hand The hand with which they performed the action.
	 * @return If the activation was successful. {@code true} will trigger the hand-swinging animation.
	 */
	@Deprecated
	public boolean activate(MultipartState state, MultipartView view, PlayerEntity player, Hand hand) {
		return false;
	}

}
