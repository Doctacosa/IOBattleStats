# IOBattleStats
Record various gameplay stats on a Minecraft server to better track the activity of the players. This is logged to a MySQL database and can then be used for other purposes like finding sources of griefing, grant rewards for completing tasks, build player profiles, or anything of your choosing.


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
