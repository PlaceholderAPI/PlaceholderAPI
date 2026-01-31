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
    - A
        - *No Expansions*
    - B
        - *No Expansions*
    - C
        - *No Expansions*
    - D
        - *No Expansions*
    - E
        - *No Expansions*
    - F
        - *No Expansions*
    - G
        - *No Expansions*
    - H
        - *No Expansions*
    - I
        - *No Expansions*
    - J
        - *No Expansions*
    - K
        - *No Expansions*
    - L
        - *No Expansions*
    - M
        - *No Expansions*
    - N
        - *No Expansions*
    - O
        - *No Expansions*
    - P
        - [Player](#player)
    - Q
        - *No Expansions*
    - R
        - *No Expansions*
    - S
        - *No Expansions*
    - T
        - *No Expansions*
    - U
        - *No Expansions*
    - V
        - *No Expansions*
    - W
        - *No Expansions*
    - X
        - *No Expansions*
    - Y
        - *No Expansions*
    - Z
        - *No Expansions*

----

- [Plugin-placeholders](#plugin-placeholders)
    - A
        - *No Expansions*
    - B
        - *No Expansions*
    - C
        - *No Expansions*
    - D
        - *No Expansions*
    - E
        - *No Expansions*
    - F
        - *No Expansions*
    - G
        - *No Expansions*
    - H
        - *No Expansions*
    - I
        - *No Expansions*
    - J
        - *No Expansions*
    - K
        - *No Expansions*
    - L
        - *No Expansions*
    - M
        - *No Expansions*
    - N
        - *No Expansions*
    - O
        - *No Expansions*
    - P
        - *No Expansions*
    - Q
        - *No Expansions*
    - R
        - *No Expansions*
    - S
        - *No Expansions*
    - T
        - *No Expansions*
    - U
        - *No Expansions*
    - V
        - *No Expansions*
    - W
        - *No Expansions*
    - X
        - *No Expansions*
    - Y
        - *No Expansions*
    - Z
        - *No Expansions*

----

## Standalone

Expansions listed here don't need any plugin/mod or extra library to function properly, unless mentioned otherwise.  
A majority of these Expansions are maintained by the PlaceholderAPI team and can be considered *official*.

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
