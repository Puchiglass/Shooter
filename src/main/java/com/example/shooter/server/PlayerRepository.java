package com.example.shooter.server;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PlayerRepository {

    public static PlayerStatistic getPlayerStat(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<PlayerStatistic> res =(List<PlayerStatistic>)session.createQuery("FROM PlayerStatistic WHERE name = :name").setParameter("name", name).list();
        session.close();
        if (res.isEmpty()) {
            PlayerStatistic newPlayer = new PlayerStatistic(name);
            addNewPlayer(newPlayer);
            return newPlayer;
        }
        return res.getFirst();
    }

    private static void addNewPlayer(PlayerStatistic player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(player);
        transaction.commit();
        session.close();
    }

    public static void increaseNumWins(PlayerStatistic player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(player);
        transaction.commit();
        session.close();
    }

    public static List<PlayerStatistic> getLeaderboard() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<PlayerStatistic> res =(List<PlayerStatistic>)session.createQuery("FROM PlayerStatistic ORDER BY numWins DESC").list();
        session.close();
        return res;
    }
}
