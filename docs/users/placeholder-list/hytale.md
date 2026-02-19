# Hytale

This is a list of all available placeholders for the Hytale version of PlaceholderAPI.  
For the Minecraft version, visit [this page](minecraft.md).

A download-command may be found in the infobox located below the title of the Expansion.  
Should `Built into mod` be displayed is the Expansion included in the mod it depends on. Should a URL be shown does it mean you have to download it manually and add it to the `expansions` folder yourself.

/// note
This placeholder list is provided "as-is" without any guarantee of being accurate and/or up-to-date.

Page is only updated on request. We recommend contributing to this list by [making a Pull request](https://github.com/PlaceholderAPI/PlaceholderAPI/pulls).  
Further details on how to contribute to this list or the wiki as a whole can be found on the [README file of the Wiki](https://github.com/PlaceholderAPI/PlaceholderAPI/blob/wiki/README.md).
///

- [Standalone](#standalone)
    - **A**
        - *No Expansions*
    - **B**
        - *No Expansions*
    - **C**
        - **[ChangeOutput](#changeoutput)**
    - **D**
        - *No Expansions*
    - **E**
        - *No Expansions*
    - **F**
        - *No Expansions*
    - **G**
        - *No Expansions*
    - **H**
        - *No Expansions*
    - **I**
        - *No Expansions*
    - **J**
        - **[Javascript](#javascript)**
    - **K**
        - *No Expansions*
    - **L**
        - *No Expansions*
    - **M**
        - **[Math](#math)**
    - **N**
        - *No Expansions*
    - **O**
        - *No Expansions*
    - **P**
        - **[Player](#player)**
        - **[Progress](#progress)**
    - **Q**
        - *No Expansions*
    - **R**
        - **[RNG](#rng)**
    - **S**
        - **[Server](#server)**
        - **[String](#string)**
    - **T**
        - *No Expansions*
    - **U**
        - *No Expansions*
    - **V**
        - *No Expansions*
    - **W**
        - **[World](#world)**
    - **X**
        - *No Expansions*
    - **Y**
        - *No Expansions*
    - **Z**
        - *No Expansions*

----

- [Mod-placeholders](#Mod-placeholders)
    - **A**
        - *No Expansions*
    - **B**
        - *No Expansions*
    - **C**
        - **[CleanPing](#cleanping)**
    - **D**
        - **[Declares Advanced Jobs](#declares-advanced-jobs)**
    - **E**
        - **[EconomyAPI](#economyapi)**
        - **[EliteEssentials](#eliteessentials)**
        - **[EssentialsCore](#essentialscore)**
        - **[EssentialsPlus](#essentialsplus)**
    - **F**
        - **[FlectonePulse](#flectonepulse)**
    - **G**
        - **[Guilds](#guilds)**
    - **H**
        - **[HyFactions](#hyfactions)**
        - **[HyVotifier](#hyvotifier)**
        - **[HyperPerms](#hyperperms)**
        - **[HyperFactions](#hyperfactions)**
    - **I**
        - *No Expansions*
    - **J**
        - *No Expansions*
    - **K**
        - *No Expansions*
    - **L**
        - **[LevelingCore](#levelingcore)**
        - **[LuckPerms](#luckperms)**
    - **M**
        - **[MMOSkillTree](#mmoskilltree)**
        - **[MysticNametags](#mysticnametags)**
    - **N**
        - *No Expansions*
    - **O**
        - *No Expansions*
    - **P**
        - **[Prekoyte's Powerlevels](#prekoytespowerlevels)**
    - **Q**
        - *No Expansions*
    - **R**
        - **[Rankup System](#rankupsystem)**
        - **[RPGLevelling](#rpglevelling)**
    - **S**
        - **[SimpleClaims](#simpleclaims)**
    - **T**
        - *No Expansions*
    - **U**
        - *No Expansions*
    - **V**
        - **[VaultUnlocked](#vaultunlocked)**
    - **W**
        - **[WiFlowAPI](#wiflowapi)**
    - **X**
        - *No Expansions*
    - **Y**
        - *No Expansions*
    - **Z**
        - *No Expansions*

----

## Standalone

Expansions listed here don't need any plugin/mod or extra library to function properly, unless mentioned otherwise.  
A majority of these Expansions are maintained by the PlaceholderAPI team and can be considered *official*.

----

### **ChangeOutput**
/// command | papi ecloud download changeoutput
///

Allows you to change the output based on what other placeholders return. 

More information can be found on the [GitHub Repository](https://github.com/PlaceholderAPI/Expansion-ChangeOutput/tree/hytale)

```
%changeoutput_<options>_input:<input>_matcher:<matcher>_ifmatch:<output-if-matched>_else:<output-if-not-matched>%
```

- `<options>`
- equals - match the input exactly
- ignorecase - match the input while ignoring cases
- ignorecolor - match the input while ignoring colour codes
- contains - check if the match contains input
- \>= - check if the input is larger than or equal to the matcher
- \> - check if the input is larger than the matcher
- <= - check if the input is less than or equal to the matcher
- < - check if the input is less than the matcher
- `<input>` - this is your text that you wish to replace
- `<match>` - this is the text we will be looking for to meet the conditions
- `<output-if-matched>` - if the input meets the condition, this text will be displayed
- `<output-if-not-matched>` - if the input does not meet the condition, this text will be displayed instead

*All arguments can be replaced with other placeholders, wrapped in* `{}`

----

### **[Javascript](https://ecloud.placeholderapi.com/expansions/javascript-1/)**
/// failure | Unverified
Due to potential security issues this expansion is currently unverified. Use at your own risk.
///

Gives you a way, to use javascript, to give a different output, depending on conditions.

```
%javascript_<your placeholder identifier>%
```

----

### **Math**
/// command | papi ecloud download Math
///

Allows evaluation of basic and complex mathematical Expressions through EvalEx.

```
%math_<expression>%
%math_[decimals]:[rounding-mode]_<expression>%
```

----

### **Player**
/// command | papi ecloud download Player
///

```
%player_uuid%
%player_username%
%player_language%
%player_world_uuid%
%player_x%
%player_y%
%player_z%
%player_yaw%
%player_pitch%
%player_has_played_before%
%player_name%
%player_gamemode%
%player_world%
%player_world_displayname%
%player_world_worldgen_type%
%player_world_worldgen_name%
%player_biome%
%player_item_in_hand%
%player_item_in_hand_quantity%
%player_item_in_hand_durability%
%player_item_in_hand_broken%
%player_item_in_hand_unbreakable%
%player_current_fall_distance%
%player_view_radius%
%player_client_view_radius%
%player_since_last_spawn_nanos%
%player_mount_entity_id%
%player_is_collidable%
%player_health%
%player_health_max%
%player_health_min%
%player_ammo%
%player_ammo_max%
%player_ammo_min%
%player_stamina%
%player_stamina_max%
%player_stamina_min%
%player_mana%
%player_mana_max%
%player_mana_min%
%player_oxygen%
%player_oxygen_max%
%player_oxygen_min%
%player_signature_energy%
%player_signature_energy_max%
%player_signature_energy_min%
%player_has_permission_<permission>%
```

----

### **Progress**
/// command | papi ecloud download progress
///

More info about this expansion can be found on the [GitHub-Repository](https://github.com/PlaceholderAPI/Progress-Expansion).

```
%progress_bar_{placeholder}%
%progress_bar_{placeholder}_c:<completed Symbol>%
%progress_bar_{placeholder}_p:<progress Symbol>%
%progress_bar_{placeholder}_r:<remaining Symbol>%
%progress_bar_{placeholder}_l:<max length>%
%progress_bar_{placeholder}_m:<max value>%
%progress_bar_{placeholder}_fullbar:<text>%

# Example
%progress_bar_{placeholder}_c:X_p:+_r:-_l:10_m:100_fullbar:Completed!%
```

----

### **RNG**
/// command | papi ecloud download rng
///

```
%rng_random%
%rng_online_player%
%rng_last_generated%
%rng_<minimum>,<maximum>%
%rng_list:<num1>;<weight1>,<num2>;<weight2>%

NOTE: You can use embedded placeholders e.g.:
%rng_{player_health},{player_exp}%
```

----

### **Server**
/// command | papi ecloud download Server
///

```
%server_name%
%server_online%
%server_worlds%
%server_max_players%
%server_max_view_radius%
%server_motd%
%server_default_gamemode%
%server_default_world%
%server_rate_limit_enabled%
%server_rate_limit_packets_per_second%
%server_rate_limit_burst_capacity%
%server_is_booting%
%server_is_shutting_down%
%server_boot_timestamp%
%server_uptime_millis%
%server_uptime_seconds%
%server_uptime%
%server_plugin_count%
```

----

### **String**
/// command | papi ecloud download String
///

More info about the expansion can be found on the [Github-Repository](https://github.com/BlitzOffline/StringExpansion).

```
%string_equals_<string>_<match>%
%string_equalsIgnoreCase_<string>_<match>%
%string_contains_<string>_<match>%
%string_containsIgnoreCase_<string>_<match>%
%string_charAt_<index>_<string>%
%string_indexOf_<string>_<match>%
%string_lastIndexOf_<string>_<match>%
%string_substring_<startIndex>_<string>%
%string_substring_<startIndex>,<endIndex>_<string>%
%string_charAt_<index>_<string>%
%string_shuffle_<string>%
%string_uppercase_<string>%
%string_lowercase_<string>%
%string_sentencecase_<string>%
%string_capitalize_<string>%
%string_length_<string>%
%string_random_<string1>,<string2>,<string3>,<etc>%
%string_replaceCharacters_<configuration>_<string>%
%string_alternateuppercase_<string>%
%string_startswith_<string>_<match>%
%string_endswith_<string>_<match>%
%string_trim_<string>%
%string_occurences_count_<string>_<match>%
```

----

### **World**
/// command | papi ecloud download World
///

```
%world_total%
%world_biome%
%world_time%
%world_timein12%
%world_fulltime%
%world_dayprogress%
%world_moonphase%
%world_sunlightfactor%
%world_date%
%world_sunddirection_x%
%world_sunddirection_y%
%world_sunddirection_z%
%world_name_<world>%
%world_uuid_<world>%
%world_seed_<world>%
%world_canpvp_<world>%
%world_spawnnpc_<world>%
%world_npcfrozen_<world>%
%world_falldamage_<world>%
%world_objectivemarkers_<world>%
%world_entities_<world>%
%world_players_<world>%
%world_players_<world>_<group>%
%world_haspermission_<world>_<permission>%
%world_playerexist_<world>_<playername>%
%world_recentjoin_<world>%
%world_recentquit_<world>%
%world_displayname_<world>%
```

----

## Mod-placeholders

Expansions listed here require the linked resource (plugin/mod) to work properly.

Most of the listed Expansions are NOT made and maintained by the PlaceholderAPI team.  
Please see ?510 for a list of all expansions officially maintained by the PlaceholderAPI team.

### **[CleanPing](https://www.curseforge.com/hytale/mods/cleanping)**
/// integrated | Built into mod
///

```
%cleanping_ping%
%cleanping_coloured_ping%
%cleanping_ping_<username>%
%cleanping_coloured_ping_<username>%
%cleanping_difference_<username>% - difference between requesting player and requested player ping
%cleanping_difference_<username1>_<username2>%
```

----

### **[Declares-Advanced-Jobs](https://www.curseforge.com/hytale/mods/declares-advanced-jobs)**
/// integrated | Built into mod
///

```
%jobs_current_job%
%jobs_level_<job>%
%jobs_xp_<job>%
%jobs_income_<job>%
```

----

### **[EconomyAPI](https://www.curseforge.com/hytale/mods/economyapi)**
/// integrated | Built into mod
///

```
%economyapi_balance%
%economyapi_formatted_balance%
```

----

### **[EliteEssentials](https://www.curseforge.com/hytale/mods/eliteessentials)**
/// integrated | Built into mod
///

```
%eliteessentials_economy_enabled%
%eliteessentials_using_external_economy%
%eliteessentials_currency_name%
%eliteessentials_currency_name_plural%
%eliteessentials_currency_symbol%
%eliteessentials_balance%
%eliteessentials_god%
%eliteessentials_vanished%
%eliteessentials_homes_num%
%eliteessentials_homes_max%
%eliteessentials_homes_names%
%eliteessentials_all_kits_num%
%eliteessentials_all_kits_names%
%eliteessentials_allowed_kits_num%
%eliteessentials_allowed_kits_names%
%eliteessentials_all_warps_num%
%eliteessentials_all_warps_names%
%eliteessentials_allowed_warps_num%
%eliteessentials_allowed_warps_names%
%eliteessentials_home_<name>_name%
%eliteessentials_home_<name>_createdat%
%eliteessentials_home_<name>_coords%
%eliteessentials_home_<name>_x%
%eliteessentials_home_<name>_y%
%eliteessentials_home_<name>_z%
%eliteessentials_home_<name>_yaw%
%eliteessentials_home_<name>_pitch%
%eliteessentials_home_<name>_world%
%eliteessentials_kit_<id>_name%
%eliteessentials_kit_<id>_id%
%eliteessentials_kit_<id>_description%
%eliteessentials_kit_<id>_icon%
%eliteessentials_kit_<id>_cooldown%
%eliteessentials_kit_<id>_remainingcooldown%
%eliteessentials_kit_<id>_items%
%eliteessentials_warp_<name>_name%
%eliteessentials_warp_<name>_description%
%eliteessentials_warp_<name>_permission%
%eliteessentials_warp_<name>_createdat%
%eliteessentials_warp_<name>_createdby%
%eliteessentials_warp_<name>_coords%
%eliteessentials_warp_<name>_x%
%eliteessentials_warp_<name>_y%
%eliteessentials_warp_<name>_z%
%eliteessentials_warp_<name>_yaw%
%eliteessentials_warp_<name>_pitch%
%eliteessentials_warp_<name>_world%
```

----

### **[EssentialsCore](https://www.curseforge.com/hytale/mods/essentials-core)**
/// integrated | Built into mod
///

/// warning | [Subject to PR approval](https://github.com/nhulston/Essentials/pull/14)
///

```
%essentials_max_homes%                    Max homes                                               
%essentials_homes_num%                    Number of homes                                         
%essentials_homes_names%                  Names of homes (split by ", ")                          
%essentials_all_kits_num%                 Number of all kits on server                            
%essentials_all_kits_names%               Names of all kits on server (split by ", ")             
%essentials_allowed_kits_num%             Number of kits this player can access                   
%essentials_allowed_kits_names%           Names of kits this player can access (split by ", ")    
%essentials_all_warps_num%                Number of all warps on server                           
%essentials_all_warps_names%              Names of all warps on server (split by ", ")            
%essentials_<warp/home>_<name>_world%   World name of a particular warp/home                    
%essentials_<warp/home>_<name>_coords%  Coords of a particular warp/home (x y z)                
%essentials_<warp/home>_<name>_x%       X coord of a warp/home                                  
%essentials_<warp/home>_<name>_y%       Y coord of a warp/home                                  
%essentials_<warp/home>_<name>_z%       Z coord of a warp/home                                  
%essentials_<warp/home>_<name>_yaw%     Yaw of a warp/home                                      
%essentials_<warp/home>_<name>_pitch%   Pitch of a warp/home                                    
%essentials_warp_<name>_allowed%         Can this player access this warp                        
%essentials_home_<name>_createdat%       Timestamp of when a home was created                    
%essentials_kit_<name>_name%             Display name of a kit                                   
%essentials_kit_<name>_id%               Id of a kit                                             
%essentials_kit_<name>_type%             Type of a kit                                           
%essentials_kit_<name>_cooldown%         Cooldown of a kit                                       
%essentials_kit_<name>_isreplacemode%    Whether kit has replacemode set to true/false           
%essentials_kit_<name>_itemsnum%         Number of items in a kit                                
%essentials_kit_<name>_allowed%          Can this player access the kit  
```

----

### **[EssentialsPlus](https://www.curseforge.com/hytale/mods/essentials-plus)**
/// integrated | Built into mod
///

```
%essentialsplus_player_balance%
%essentialsplus_player_balance_formatted%
%essentialsplus_player_first_join%
%essentialsplus_player_last_join%
%essentialsplus_player_playtime%
%essentialsplus_player_session_time%
%essentialsplus_player_homes_count%
%essentialsplus_player_homes_max%
%essentialsplus_player_homes_<number>%
%essentialsplus_player_homes_<number>_world%
%essentialsplus_player_homes_<number>_x%
%essentialsplus_player_homes_<number>_y%
%essentialsplus_player_homes_<number>_z%
%essentialsplus_player_is_muted%
%essentialsplus_player_is_frozen%
%essentialsplus_player_is_vanished%
%essentialsplus_player_is_flying%
%essentialsplus_server_homes_total%
%essentialsplus_server_warps_total%
%essentialsplus_server_kits_total%
%essentialsplus_server_players_total%
```

----

### **[FlectonePulse](https://www.curseforge.com/hytale/mods/flectonepulse)**
/// integrated | Built into mod
///

```
%flectonepulse_advancement%                 Returns true if display is enabled, otherwise empty
%flectonepulse_afk%                         Returns true if display is enabled, otherwise empty
%flectonepulse_afk_suffix%                  Returns AFK suffix
%flectonepulse_auto%                        Returns true if display is enabled, otherwise empty
%flectonepulse_chat_name%                   Returns the selected chat type, default by default
%flectonepulse_death%                       Returns true if display is enabled, otherwise empty
%flectonepulse_discord%                     Returns true if display is enabled, otherwise empty
%flectonepulse_fcolor_number%               Returns the player's custom color
%flectonepulse_fcolor_out_number%           Returns the player's custom OUT color
%flectonepulse_fcolor_see_number%           Returns the player's custom SEE color
%flectonepulse_greeting%                    Returns true if display is enabled, otherwise empty
%flectonepulse_ip%                          Returns the player's IP address
%flectonepulse_join%                        Returns true if display is enabled, otherwise empty
%flectonepulse_locale%                      Returns the player's current locale
%flectonepulse_online%                      Returns the number of players on the server
%flectonepulse_ping%                        Returns the player's ping
%flectonepulse_player%                      Returns the player's regular name
%flectonepulse_quit%                        Returns true if display is enabled, otherwise empty
%flectonepulse_stream_prefix%               Returns the player's stream prefix
%flectonepulse_spy_status                   Returns a string if spy mode is enabled, otherwise empty
%flectonepulse_telegram%                    Returns true if display is enabled, otherwise empty
%flectonepulse_tps%                         Returns the server TPS
%flectonepulse_twitch%                      Returns true if display is enabled, otherwise empty
%flectonepulse_world_prefix%                Returns the player's world prefix
%flectonepulse_mute_suffix%                 Returns the player's mute suffix
%flectonepulse_command_ball%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_ban%                 Returns true if display is enabled, otherwise empty
%flectonepulse_command_broadcast%           Returns true if display is enabled, otherwise empty
%flectonepulse_command_coin%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_dice%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_do%                  Returns true if display is enabled, otherwise empty
%flectonepulse_command_kick%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_mail%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_me%                  Returns true if display is enabled, otherwise empty
%flectonepulse_command_mute%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_poll%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_reply%               Returns true if display is enabled, otherwise empty
%flectonepulse_command_rockpaperscissors%   Returns true if display is enabled, otherwise empty
%flectonepulse_command_spy%                 Returns true if display is enabled, otherwise empty
%flectonepulse_command_stream%              Returns true if display is enabled, otherwise empty
%flectonepulse_command_tell%                Returns true if display is enabled, otherwise empty
%flectonepulse_command_tictactoe%           Returns true if display is enabled, otherwise empty
%flectonepulse_command_translateto%         Returns true if display is enabled, otherwise empty
%flectonepulse_command_try%                 Returns true if display is enabled, otherwise empty
%flectonepulse_command_warn%                Returns true if display is enabled, otherwise empty
```

----

### **[Guilds](https://www.curseforge.com/hytale/mods/guilds)**
/// integrated | Built into mod
///

```
%guild_name%
%guild_tag%
%guild_rank%
%guild_leader%
%guild_member_count%
%guild_online_count%
%guild_claims%
%guild_lots%
%guild_bank_balance%
%guild_provider%
%guild_preferred_economy%
```

----

### **[HyFactions](https://www.curseforge.com/hytale/mods/hyfactions)**
/// command | papi ecloud download HyFactions
///

Relational:
```
%rel_factions_relation% - Relation type (ally, enemy, etc)
%rel_factions_relation_color% - green, white, red
```

Standard:
```
%factions_player_has_faction% - yes/no
%factions_player_faction% - faction name
%factions_player_faction_id% - faction uuid
%factions_player_faction_rank% - rank in faction
%factions_player_power% - power level of player
%factions_party_color% - party color (number)
%factions_party_name% - party name
%factions_party_id% - party id
%factions_party_description% - party description
%factions_party_created% - date of party creation
%factions_party_max_claims% - max number of claims for party
%factions_party_claims% - number of claims by party
%factions_party_members% - number of members in party
%factions_party_owner_id% - owner uuid
%factions_party_owner_name% - owner username
%factions_faction_home_dimension% - dimension name of faction home
%factions_faction_home_x% - 2 D.P. x coord of faction home
%factions_faction_home_y% - 2 D.P. y coord of faction home
%factions_faction_home_z% - 2 D.P. z coord of faction home
%factions_faction_home_coords% - 2 D.P. x y z coords of faction home
%factions_faction_home_yaw% - 2 D.P. yaw of faction home
%factions_faction_home_pitch% - 2 D.P. pitch of faction home
%factions_faction_color% - number of faction color
%factions_faction_description% - faction description
%factions_faction_max_claims% - max claims allowed by faction
%factions_faction_owner_id% - faction owner uuid
%factions_faction_owner_name% - faction owner username
%factions_faction_created% - date of faction creation
%factions_faction_members% - number of members in faction
%factions_faction_relations% - number of relations with other factions
%factions_faction_allies% - number of allies
%factions_faction_neutrals% - number of neutrals
%factions_faction_enemies% - number of enemies
%factions_faction_claims% - number of claims by faction
%factions_faction_total_power% - total power of faction
```

----

### **[HyVotifier](https://www.curseforge.com/hytale/mods/hytale-votifier)**
/// command | papi ecloud download HyVotifier
///

```
%hyvotifier_offline_votes%
%hyvotifier_has_offline_votes%
%hyvotifier_offline_votes_enabled%
%hyvotifier_offline_votes_maxclaims%
%hyvotifier_offline_votes_maxlifetimehrs%
%hyvotifier_milestones_enabled%
%hyvotifier_milestone_votecount%
%hyvotifier_next_milestone%
%hyvotifier_leaderboard_votes%
%hyvotifier_leaderboard_position%
%hyvotifier_leaderboard_top_<position>_name% (replace <position> with any positive integer, e.g., 1, 2, 3, ...)
%hyvotifier_leaderboard_top_<position>_votes% (replace <position> with any positive integer, e.g., 1, 2, 3, ...)
```

----

### **[HyperPerms](https://www.curseforge.com/hytale/mods/hyperperms)**
/// integrated | Built into mod
///

```
%hyperperms_prefix%
%hyperperms_suffix%
%hyperperms_group%
%hyperperms_group_display%
%hyperperms_groups%
%hyperperms_group_count%
%hyperperms_weight%
%hyperperms_has_<permission>%
%hyperperms_in_group_<name>%
```

----

### **[HyperFactions](https://www.curseforge.com/hytale/mods/hyperfactions)**
/// integrated | Built into mod
///

Relational:
```
soon:tm:
```

Standard:
```
Player faction:
   %factions_has_faction% - Whether player has a faction (yes/no)
   %factions_name% - Faction name
   %factions_faction_id% - Faction UUID
   %factions_tag% - Faction tag (short identifier)
   %factions_display% - Tag or name based on tagDisplay config
   %factions_color% - Faction color code
   %factions_role% - Player's role (Leader/Officer/Member)
   %factions_description% - Faction description
   %factions_leader% - Faction leader's name
   %factions_leader_id% - Faction leader's UUID
   %factions_open% - Whether faction is open (true/false)
   %factions_created% - Faction creation date (yyyy-MM-dd)

Power:
   %factions_power% - Player's current power
   %factions_maxpower% - Player's max power
   %factions_power_percent% - Player's power percentage
   %factions_faction_power% - Faction's total power
   %factions_faction_maxpower% - Faction's max power
   %factions_faction_power_percent% - Faction's power percentage
   %factions_raidable% - Whether faction is raidable (true/false)

Territory:
   %factions_land% - Number of claimed chunks
   %factions_land_max% - Max claimable chunks
   %factions_territory% - Faction owning current chunk
   %factions_territory_type% - Territory type at current location

Faction home:
   %factions_home_world% - World name of faction home
   %factions_home_x% - X coordinate of faction home (2 d.p.)
   %factions_home_y% - Y coordinate of faction home (2 d.p.)
   %factions_home_z% - Z coordinate of faction home (2 d.p.)
   %factions_home_coords% - X, Y, Z coordinates of faction home (2 d.p.)
   %factions_home_yaw% - Yaw of faction home (2 d.p.)
   %factions_home_pitch% - Pitch of faction home (2 d.p.)

Members and relations:
   %factions_members% - Total member count
   %factions_members_online% - Online member count
   %factions_allies% - Number of allied factions
   %factions_enemies% - Number of enemy factions
   %factions_neutrals% - Number of neutral relations
   %factions_relations% - Total number of relations
```

----

### **[LevelingCore](https://www.curseforge.com/hytale/mods/levelingcore)**
/// integrated | Built into mod
///

```
%levelingcore_level%
%levelingcore_xp%
%levelingcore_xp_to_level%
%levelingcore_ability_points%
%levelingcore_available_ability_points%
%levelingcore_str%
%levelingcore_agi%
%levelingcore_per%
%levelingcore_vit%
%levelingcore_int%
%levelingcore_con%
```

----

### **[LuckPerms](https://www.curseforge.com/hytale/mods/luckperms)**
/// command | papi ecloud download LuckPerms
///

```
%luckperms_prefix%
%luckperms_suffix%
%luckperms_meta_<metakey>%
%luckperms_prefix_element_<element>%
%luckperms_suffix_element_<element>%
%luckperms_context_<contextkey>%
%luckperms_groups%
%luckperms_primary_group_name%
%luckperms_has_permission_<permission>%
%luckperms_inherits_permission_<permission>%
%luckperms_check_permission_<permission>%
%luckperms_in_group_<group>%
%luckperms_inherits_group_<group>%
%luckperms_on_track_<track>%
%luckperms_has_groups_on_track_<track>%
%luckperms_highest_group_by_weight%
%luckperms_lowest_group_by_weight%
%luckperms_first_group_on_tracks_<tracks>%
%luckperms_last_group_on_tracks_<tracks>%
%luckperms_expiry_time_<permission>%
%luckperms_inherited_expiry_time_<permission>%
%luckperms_group_expiry_time_<groupname>%
```

----

### **[MMOSkillTree](https://www.curseforge.com/hytale/mods/mmo-skill-tree)**
/// command | papi ecloud download mmoskilltree
///

```
%mmoskilltree_has_skill_data%
%mmoskilltree_all_xp%
%mmoskilltree_all_levels%
%mmoskilltree_total_xp%
%mmoskilltree_level_total%
%mmoskilltree_calculate_level_from_xp_<xp>%
%mmoskilltree_get_xp_for_level_<level>%
%mmoskilltree_level_progress_<skill>%
%mmoskilltree_xp_<skill>%
%mmoskilltree_level_<skill>%
```

----

### **[MysticNametags](https://www.curseforge.com/hytale/mods/mysticnametags)**
/// integrated | Built into mod
///

```
%mystictags_tag%
%mystictags_tag_plain%
%mystictags_full%
%mystictags_full_plain%
```

----

### **[PrekoytesPowerlevels](https://www.curseforge.com/hytale/mods/prekoytes-powerlevels)**
/// integrated | Built into mod
///

```
%powerlevel_power% - Integer power.
%powerlevel_powerdecimal% - Power with one decimal.
%powerlevel_best% - Personal best.
%powerlevel_color% - Color code from colors.json.
%powerlevel_rank% - Rank name from colors.json.
```

----

### **[RankupSystem](https://www.curseforge.com/hytale/mods/rankup-system)**
/// integrated | Built into mod
///

```
%rankup_rank%
%rankup_next_rank%
%rankup_cost%
```

----

### **[RPGLevelling](https://www.curseforge.com/hytale/mods/rpg-leveling-and-stats)**
/// command | papi ecloud download RPGLevelling
///

```
%rpglevelling_xp%
%rpglevelling_level%
%rpglevelling_is_max%
%rpglevelling_maxlevel%
%rpglevelling_xpnextlevel%
```

----

### **[SimpleClaims](https://www.curseforge.com/hytale/mods/simple-claims)**
/// integrated | Built into mod
///

```
%simpleclaims_parties_total%
%simpleclaims_can_place_blocks%
%simpleclaims_can_interact_blocks%
%simpleclaims_can_break_blocks%
%simpleclaims_can_interact_chest%
%simpleclaims_can_interact_bench%
%simpleclaims_can_interact_chair%
%simpleclaims_can_interact_door%
%simpleclaims_can_interact_portal%
%simpleclaims_can_enter%
%simpleclaims_can_friendly_fire%
%simpleclaims_can_pvp%
%simpleclaims_party_name%
%simpleclaims_party_description%
%simpleclaims_party_id%
%simpleclaims_party_size%
%simpleclaims_party_color%
%simpleclaims_party_created%
%simpleclaims_party_maxclaims%
%simpleclaims_party_modified%
%simpleclaims_party_owner_uuid%
%simpleclaims_party_owner_name%
%simpleclaims_party_can_place_blocks%
%simpleclaims_party_can_interact_blocks%
%simpleclaims_party_can_break_blocks%
%simpleclaims_party_can_interact_chest%
%simpleclaims_party_can_interact_bench%
%simpleclaims_party_can_interact_chair%
%simpleclaims_party_can_interact_door%
%simpleclaims_party_can_interact_portal%
%simpleclaims_party_can_enter%
%simpleclaims_party_can_friendly_fire%
%simpleclaims_party_can_pvp%
%simpleclaims_party_allies_total%
%simpleclaims_party_allies_uuids%
%simpleclaims_party_allies_names%
%simpleclaims_party_claims%
%simpleclaims_party_partyallied_<partyId>%
%simpleclaims_party_playerallied_<playerName>%
```

----

### **[VaultUnlocked](https://www.curseforge.com/hytale/mods/vaultunlocked)**
/// command | papi ecloud download vaultunlocked
///

```
%vault_eco_balance%
%vault_eco_balance_<number>dp%
%vault_eco_balance_fixed%
%vault_eco_balance_formatted%
%vault_eco_balance_commas%
%vault_group%
%vault_group_capital%
%vault_groups%
%vault_groups_capital%
%vault_prefix%
%vault_suffix%
%vault_groupprefix%
%vault_groupprefix_<position>%
%vault_groupsuffix%
%vault_groupsuffix_<position>%
%vault_hasgroup_<group>%
%vault_inprimarygroup_<group>%
```

----

### **[WiFlowAPI](https://www.curseforge.com/hytale/mods/wiflows-placeholderapi)**
/// command | papi ecloud download WiFlowAPI
///
Lets you use placeholders from WiFlow's API
A list of placeholders can be found [here](https://docs.wiflow.dev/hytale-plugins/wiflow-placeholderapi/expansions).
```
%wiflowapi_{placeholder}%
```

