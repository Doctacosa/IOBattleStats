# IOBattleStats

![Logo](https://www.interordi.com/images/plugins/iobattlestats-96.png)

Record various gameplay stats on a Minecraft server to better track the activity of the players. This is logged to a MySQL database and can then be used for other purposes like finding sources of griefing, grant rewards for completing tasks, build player profiles, or anything of your choosing. Multiple servers on a network can share the same database.

No visualisation tools are included, this is left up to the server owner.

This plugin was developed out of a growing frustration from other plugins no longer getting updated, or giving inaccurate results.


## Basic table format

`uuid`: The player's unique ID  
`amount`: The amount of times this task was completed  
`world`: The name of the world the action took place in, automatically read from the server  

Additional fields are added on the tables to record relevant data, while some others have a different format.

For example, the more complex stats_io_deaths:
`source`: the source of the death (player or entity)  
`target`: the target of the death (player or entity)  
`world`: the world where the action took place  
`cause`: the cause of the death (which weapon or item)  
`weapon_name`: the weapon's custom name, if set  
`amount`: the amount of deaths matching all of the above  
`player_source`: if the player was the cause of the death  
`player_target`: if it was a player that died  


## Database tables and tracked values

All table names are prefixed with `stats_io_` to avoid naming conflicts with other plugins.

`arrows`  
`beds_entered`  
`blocks_broken`  
`blocks_placed`  
`buckets_emptied`  
`buckets_filled`  
`change_world`  
`chat_words`  
`commands`  
`consumed`  
`crafted`  
`damage`  
`deaths`  
`eggs_thrown`  
`fish_caught`  
`item_broken`  
`item_dropped`  
`item_picked_up`  
`joins`  
`kicks`  
`move`  
`pvp_streak`  
`shears`  
`teleports`  
`trades`  
`xp_gained`  


## Configuration

`database.host`: Database host  
`database.port`: Database port  
`database.base`: Database name  
`database.username`: Database username  
`database.password`: Database password  


## Commands

`givehead PLAYER`: Obtain the skull of the specified player


## Permissions

`iobattlestats.track`: Add to any player or group to be logged  
`iobattlestats.givehead`: Determine who is allowed to use the `/givehead` command  
