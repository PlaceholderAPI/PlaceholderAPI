# Hytale

This is a list of all available placeholders for the Hytale version of PlaceholderAPI.  
For the Minecraft version, visit [this page](minecraft.md).

A download-command may be found in the infobox located below the title of the Expansion.  
Should `Built into Plugin` be displayed is the Expansion included in the Plugin it depends on. Should a URL be shown does it mean you have to download it manually and add it to the `expansions` folder yourself.

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
        - *No Expansions*
    - **K**
        - *No Expansions*
    - **L**
        - *No Expansions*
    - **M**
        - *No Expansions*
    - **N**
        - *No Expansions*
    - **O**
        - *No Expansions*
    - **P**
        - **[Player](#player)**
    - **Q**
        - *No Expansions*
    - **R**
        - *No Expansions*
    - **S**
        - **[Server](#server)**
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

- [Plugin-placeholders](#plugin-placeholders)
    - **A**
        - *No Expansions*
    - **B**
        - *No Expansions*
    - **C**
        - *No Expansions*
    - **D**
        - *No Expansions*
    - **E**
        - **[EliteEssentials](#eliteessentials)**
    - **F**
        - *No Expansions*
    - **G**
        - *No Expansions*
    - **H**
        - **[HyFactions](#hyfactions)**
    - **I**
        - *No Expansions*
    - **J**
        - *No Expansions*
    - **K**
        - *No Expansions*
    - **L**
        - **[LuckPerms](#luckperms)**
    - **M**
        - *No Expansions*
    - **N**
        - *No Expansions*
    - **O**
        - *No Expansions*
    - **P**
        - *No Expansions*
    - **Q**
        - *No Expansions*
    - **R**
        - **[RPGLevelling](#rpglevelling)**
    - **S**
        - *No Expansions*
    - **T**
        - *No Expansions*
    - **U**
        - *No Expansions*
    - **V**
        - *No Expansions*
    - **W**
        - **[WiFlowAPI](#wifloapi)**
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
```

----

## Plugin-placeholders

Expansions listed here require the linked resource (plugin/mod) to work properly.

Most of the listed Expansions are NOT made and maintained by the PlaceholderAPI team.  
Please see ?510 for a list of all expansions officially maintained by the PlaceholderAPI team.

### **[EliteEssentials](https://www.curseforge.com/hytale/mods/eliteessentials)**
/// integrated | Built into Plugin
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

### **[LuckPerms](https://www.spigotmc.org/resources/28140/)**
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

### **[WiFlowAPI](https://www.curseforge.com/hytale/mods/wiflows-placeholderapi)**
/// command | papi ecloud download WiFlowAPI
///
Lets you use placeholders from WiFlow's API
A list of placeholders can be found [here](https://docs.wiflow.dev/hytale-plugins/wiflow-placeholderapi/expansions).
```
%wiflowapi_{placeholder}%
```

