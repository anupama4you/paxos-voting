/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.paxosvoting;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

public class VotingDemo {

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32();
    private static final Random RANDOM = new Random();
    private static final String[] PROPOSALS = {"M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9"};

    public static void main(String[] args) throws IllegalAccessException {
        List<Acceptor> acceptors = new ArrayList<Acceptor>();
        Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9")
                .forEach(name -> acceptors.add(new Acceptor(name)));
        Proposer.vote(new Proposal(1L, null), acceptors);
    }

    private static void printInfo(String subject, String operation, String result) {
        System.out.println(subject + ":" + operation + "<" + result + ">");
    }

    private static Proposal nextProposal(long currentVoteNumber, List<Proposal> proposals) {
        long voteNumber = currentVoteNumber + 1;
        if (proposals.isEmpty()) 
            return new Proposal(voteNumber, PROPOSALS[RANDOM.nextInt(PROPOSALS.length)]);
        
        Collections.sort(proposals);
        Proposal maxVote = proposals.get(proposals.size() - 1);
        long maxVoteNumber = maxVote.getVoteNumber();
        String content = maxVote.getContent();
        if (maxVoteNumber >= currentVoteNumber) {
            throw new IllegalStateException("Illegal state maxVoteNumber");
        }
        if (content != null) {
            return new Proposal(voteNumber, content);
        } else {
            return new Proposal(voteNumber, PROPOSALS[RANDOM.nextInt(PROPOSALS.length)]);
        }

    }

    private static class Proposer {

        public static void vote(Proposal proposal, Collection<Acceptor> acceptors) throws IllegalAccessException {
            int quorum = Math.floorDiv(acceptors.size(), 2) + 1;
            int count = 0;
            while (true) {
                printInfo("VOTE_ROUND", "START", ++count + "");
                List<Proposal> proposals = new ArrayList<Proposal>();
                for (Acceptor acceptor : acceptors) {
                    Promise promise = acceptor.onPrepare(proposal);
                    if (promise != null && promise.isAck()) {
                        proposals.add(promise.getProposal());
                    }
                }
                if (proposals.size() < quorum) {
                    printInfo("PROPOSER[" + proposal + "]", "VOTE", "NOT PREPARED");
                    proposal = nextProposal(proposal.getVoteNumber(), proposals);
                    continue;
                }
                int acceptCount = 0;
                for (Acceptor acceptor : acceptors) {
                    if (acceptor.onAccept(proposal)) {
                        acceptCount++;
                    }
                }
                if (acceptCount < quorum) {
                    printInfo("PROPOSER[" + proposal + "]", "VOTE", "NOT ACCEPTED");
                    proposal = nextProposal(proposal.getVoteNumber(), proposals);
                    continue;
                }
                break;
            }
            printInfo("PROPOSER[" + proposal + "]", "VOTE", "SUCCESS");

        }
    }

    private static class Acceptor {

        private Proposal last = new Proposal();
        private String name;

        public Acceptor(String name) {
            this.name = name;
        }

        public Promise onPrepare(Proposal proposal) throws IllegalAccessException {
            if (Math.random() - 0.5 > 0) {
                printInfo("ACCEPTOR_" + name, "PREPARE", "NO RESPONSE");
                return null;
            }
            if (proposal == null) {
                throw new IllegalAccessException("null proposal");
            }
            if (proposal.getVoteNumber() > last.getVoteNumber()) {
                Promise response = new Promise(true, last);
                last = proposal;
                printInfo("ACCEPTOR_" + name, "PREPARE", "OK");
                return response;
            } else {
                printInfo("ACCEPTOR_" + name, "PREPARE", "REJECTED");
                return new Promise(false, null);
            }
        }

        public boolean onAccept(Proposal proposal) {
            if (Math.random() - 0.5 > 0) {
                printInfo("ACCEPTOR_" + name, "ACCEPT", "NO RESPONSE");
                return false;
            }
            printInfo("ACCEPTOR_" + name, "ACCEPT", "OK");
            return last.equals(proposal);
        }
    }

    private static class Promise {

        private final boolean ack;
        private final Proposal proposal;

        public Promise(boolean ack, Proposal proposal) {
            this.ack = ack;
            this.proposal = proposal;
        }

        public boolean isAck() {
            return ack;
        }

        public Proposal getProposal() {
            return proposal;
        }

    }

    private static class Proposal implements Comparable<Proposal> {

        private final long voteNumber;
        private final String content;

        public Proposal(long voteNumber, String content) {
            this.voteNumber = voteNumber;
            this.content = content;
        }

        public Proposal() {
            this(0, null);
        }

        public long getVoteNumber() {
            return voteNumber;
        }

        public String getContent() {
            return content;
        }

        @Override
        public int compareTo(Proposal o) {
            return Long.compare(voteNumber, o.voteNumber);
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Proposal)) {
                return false;

            }
            Proposal proposal = (Proposal) obj;
            return voteNumber == proposal.voteNumber && StringUtils.equals(content, proposal.content);
        }

        public int hashCode() {
            return HASH_FUNCTION
                    .newHasher()
                    .putLong(voteNumber)
                    .putString(content, Charsets.UTF_8)
                    .hash()
                    .asInt();

        }

        public String toString() {
            return new StringBuilder()
                    .append(voteNumber)
                    .append(':')
                    .append(content)
                    .toString();
        }
    }
}
