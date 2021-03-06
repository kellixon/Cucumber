package com.blakebr0.cucumber.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.blakebr0.cucumber.registry.ModRegistry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMeta extends ItemBase {

	private ModRegistry registry;
	public Map<Integer, MetaItem> items = new HashMap<>();
	public ArrayList<Integer> metas = new ArrayList<>();
	public List<String> tooltip = new ArrayList<>();

	public ItemMeta(String name, ModRegistry registry) {
		super(name);
		this.registry = registry;
		this.setHasSubtypes(true);
	}

	public ItemStack addItem(int meta, String name, boolean enabled) {
		if (!enabled) {
			return ItemStack.EMPTY;
		}
		if (items.containsKey(meta)) {
			return ItemStack.EMPTY;
		}
		items.put(meta, new MetaItem(name, enabled));
		metas.add(meta);
		return new ItemStack(this, 1, meta);
	}

	public ItemStack addItem(int meta, String name, boolean enabled, List<String> tooltip) {
		if (!enabled) {
			return ItemStack.EMPTY;
		}
		if (items.containsKey(meta)) {
			return ItemStack.EMPTY;
		}
		items.put(meta, new MetaItem(name, enabled, tooltip));
		metas.add(meta);
		return new ItemStack(this, 1, meta);
	}

	public ItemStack addItem(int meta, String name) {
		return addItem(meta, name, true);
	}

	public ItemStack addItem(int meta, String name, String ore, boolean enabled) {
		ItemStack stack = addItem(meta, name, enabled);
		registry.addOre(stack, ore);
		return stack;
	}

	public ItemStack addItem(int meta, String name, String ore, boolean enabled, List<String> tooltip) {
		ItemStack stack = addItem(meta, name, enabled, tooltip);
		registry.addOre(stack, ore);
		return stack;
	}

	public ItemStack addItem(int meta, String name, String ore) {
		return addItem(meta, name, ore, true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		if (isInCreativeTab(tab)) {
			for (int meta : metas) {
				stacks.add(new ItemStack(this, 1, meta));
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int i = stack.getMetadata();
		if (!items.containsKey(i)) {
			return "invalid";
		}
		MetaItem item = items.get(i);
		return this.getUnlocalizedName() + "_" + item.getName();
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		int i = stack.getMetadata();
		if (items.containsKey(i)) {
			MetaItem item = items.get(i);
			if (item.getTooltip() != null && !item.getTooltip().isEmpty()) {
				tooltip.addAll(item.getTooltip());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void initModels() {
		for (Map.Entry<Integer, MetaItem> item : items.entrySet()) {
			ModelLoader.setCustomModelResourceLocation(this, item.getKey(),
					new ModelResourceLocation(getRegistryName().toString() + "_" + item.getValue().getName()));
		}
	}

	public void init() {

	}

	public class MetaItem {
		private String name;
		private boolean enabled;
		private List<String> tooltip;

		MetaItem(String name, boolean enabled) {
			this.name = name;
			this.enabled = enabled;
		}

		MetaItem(String name, boolean enabled, List<String> tooltip) {
			this.name = name;
			this.enabled = enabled;
			this.tooltip = tooltip;
		}

		public String getName() {
			return name;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public List<String> getTooltip() {
			return tooltip;
		}
	}
}
