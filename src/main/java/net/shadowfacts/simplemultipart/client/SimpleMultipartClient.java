package net.shadowfacts.simplemultipart.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;

/**
 * @author shadowfacts
 */
public class SimpleMultipartClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModelLoadingRegistryImpl.INSTANCE.registerAppender(MultipartVariantProvider::registerMultipartModels);
		ModelLoadingRegistryImpl.INSTANCE.registerVariantProvider(MultipartVariantProvider::new);
	}

}
