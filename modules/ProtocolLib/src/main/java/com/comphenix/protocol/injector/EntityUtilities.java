/*
 *  ProtocolLib - Bukkit server library that allows access to the Minecraft protocol.
 *  Copyright (C) 2012 Kristian S. Stangeland
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program;
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307 USA
 */

package com.comphenix.protocol.injector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.comphenix.protocol.reflect.MethodUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.FieldUtils;
import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedIntHashMap;
import com.google.common.collect.Lists;

/**
 * Used to perform certain operations on entities.
 * 
 * @author Kristian
 */
class EntityUtilities {

	private static Field entityTrackerField;
	private static Field trackedEntitiesField;
	private static Field trackedPlayersField;
	private static Field trackerField;
	
	private static Method scanPlayersMethod;

	/*
	public static void updateEntity2(Entity entity, List<Player> observers) {
		EntityTrackerEntry entry = getEntityTrackerEntry(entity.getWorld(), entity.getEntityId());

		List<EntityPlayer> nmsPlayers = getNmsPlayers(observers);

		entry.trackedPlayers.removeAll(nmsPlayers);
		entry.scanPlayers(nmsPlayers);
	}
	*/

	public static void updateEntity(Entity entity, List<Player> observers) throws FieldAccessException {
		// Custom entity tracker.
	}

	/**
	 * Retrieve every client that is receiving information about a given entity.
	 * @param entity - the entity that is being tracked.
	 * @return Every client/player that is tracking the given entity.
	 * @throws FieldAccessException If reflection failed.
	 */
	public static List<Player> getEntityTrackers(Entity entity) {
		return Collections.emptyList(); // Custom entity tracker.
	}

	// Damn you, Paper
	private static Collection<?> getTrackedPlayers(Field field, Object entry) throws IllegalAccessException {
		return Collections.emptySet(); // Custom entity tracker.
	}

	/*
	private static EntityTrackerEntry getEntityTrackerEntry2(World world, int entityID) {
		WorldServer worldServer = ((CraftWorld) world).getHandle();
		EntityTracker tracker = worldServer.tracker;
		return tracker.trackedEntities.get(entityID);
	}
	*/

	private static Object getEntityTrackerEntry(World world, int entityID) throws FieldAccessException, IllegalArgumentException {
		return null; // Custom entity tracker.
	}

	/**
	 * Retrieve entity from a ID, even it it's newly created.
	 * @return The associated entity.
	 * @throws FieldAccessException Reflection error.
	 */
	public static Entity getEntityFromID(World world, int entityID) throws FieldAccessException {
		try {
			BukkitUnwrapper unwrapper = new BukkitUnwrapper();
			Object worldServer = unwrapper.unwrapItem(world);
			Object tracker;

			Class<?> worldClass = MinecraftReflection.getMinecraftClass("World");
			Method entityByIdMethod = worldClass.getMethod("getEntity", int.class);

			tracker = entityByIdMethod.invoke(worldServer, entityID);

			// If the tracker is NULL, we'll just assume this entity doesn't exist
			if (tracker != null) {
				return (Entity) MinecraftReflection.getBukkitEntity(tracker);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new FieldAccessException("Cannot find entity from ID " + entityID + ".", e);
		}
	}

	private static List<Object> unwrapBukkit(List<Player> players) {
		List<Object> output = Lists.newArrayList();
		BukkitUnwrapper unwrapper = new BukkitUnwrapper();
		
		// Get the NMS equivalent
		for (Player player : players) {
			Object result = unwrapper.unwrapItem(player);
			
			if (result != null)
				output.add(result);
			else
				throw new IllegalArgumentException("Cannot unwrap item " + player);
		}
		
		return output;
	}
}
