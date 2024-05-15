package com.example.shooter.server;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DB {
    public static PlayerStatistic get_player_stat(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<PlayerStatistic> res =(List<PlayerStatistic>)session.createQuery("FROM PlayerStatistic WHERE name = :name").setParameter("name", name).list();
        session.close();
        if (res.isEmpty()) {
            PlayerStatistic new_player = new PlayerStatistic(name);
            add_new_player(new_player);
            return new_player;
        }
        return res.getFirst();
    }

    private static void add_new_player(PlayerStatistic player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(player);
        transaction.commit();
        session.close();
    }

    public static void increase_num_wins(PlayerStatistic player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(player);
        transaction.commit();
        session.close();
    }

    public static List<PlayerStatistic> get_leaderboard() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<PlayerStatistic> res =(List<PlayerStatistic>)session.createQuery("FROM PlayerStatistic ORDER BY num_wins DESC").list();
        session.close();
        return res;
    }
}
