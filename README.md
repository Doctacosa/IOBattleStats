# IOBattleStats
Record various gameplay stats on a Minecraft server to better track the activity of the players. This is logged to a MySQL database and can then be used for other purposes like finding sources of griefing, grant rewards for completing tasks, build player profiles, or anything of your choosing.


## Configuration

`mysql.server`: Database host  
`mysql.username`: Database username  
`mysql.password`: Database password  
`mysql.base`: Database name  


## Commands

`givehead PLAYER`: Obtain the skull of the specified player


## Permissions

`iobattlestats.track`: Add to any player or group to be logged  
`iobattlestats.givehead`: Determine who is allowed to use the `/givehead` command  
