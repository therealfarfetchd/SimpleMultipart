package net.shadowfacts.simplemultipart.container;

import net.minecraft.world.BlockView;

/**
 * @author shadowfacts
 */
public class ContainerBlock extends AbstractContainerBlock {

	@Override
	public AbstractContainerBlockEntity createBlockEntity(BlockView world) {
		return new ContainerBlockEntity();
	}

}
