/*
 *     BreezeGrappler, a plugin that can make a lead turn into a grappling hook
 *     Copyright (C) 2023  dkim19375
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.dkim19375.breezegrappler.config

import kotlinx.serialization.Serializable
import org.bukkit.Sound
import org.bukkit.entity.Player

@Serializable
data class GrapplerSoundConfig(
    val type: Sound = Sound.UI_TOAST_CHALLENGE_COMPLETE,
    val pitch: Float = 2f,
    val volume: Float = 0.1F,
) {
    fun playSound(player: Player) = player.playSound(player.location, type, volume, pitch)
}