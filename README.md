# IOBattleStats

![Logo](https://www.interordi.com/images/plugins/iobattlestats-96.png)

Record various gameplay stats on a Minecraft server to better track the activity of the players. This is logged to a MySQL database and can then be used for other purposes like finding sources of griefing, grant rewards for completing tasks, build player profiles, or anything of your choosing.


## Database tables and tracked values

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
