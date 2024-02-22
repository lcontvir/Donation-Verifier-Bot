package com.lcontvir.bot.descartado;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;

public class GetPeopleWithRole extends Thread {

    private final Guild guild;

    public GetPeopleWithRole(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        Role rol = guild.getRoleById(1198475157610700911L);
        List<Member> a = guild.findMembersWithRoles(rol).get();
        for (Member m: a
        ) {
            System.out.println(m.getEffectiveName());
        }
    }
}
