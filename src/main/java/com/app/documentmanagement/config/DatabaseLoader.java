package com.app.documentmanagement.config;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.documentmanagement.entities.Author;
import com.app.documentmanagement.entities.Document;
import com.app.documentmanagement.entities.Reference;
import com.app.documentmanagement.repositories.AuthorRepository;
import com.app.documentmanagement.repositories.DocumentRepository;

@Configuration
public class DatabaseLoader {
    
    @Bean
    CommandLineRunner init(AuthorRepository authorRepository, DocumentRepository documentRepository){
        return args -> {
            ArrayList<Author> authorList = new ArrayList<>();

            authorList.add(new Author("Zeeshan","Hanif"));
            authorList.add(new Author("Zain","Hanif"));
            authorList.add(new Author("Inam","ul Haq"));
            authorList.add(new Author("Rehan","Uddin"));
            authorList.add(new Author("Taha","Ahmed"));
            authorList.add(new Author("Zia","Khan"));
            authorList.add(new Author("Daniyal","Nagori"));
            authorList.add(new Author("Mohsin","Khalid"));
            authorList.add(new Author("Arsalan","Sabir"));
            authorList.add(new Author("Taha","Shahid"));

            for (Author author : authorList) {
                authorRepository.save(author);
            }

            ArrayList<Reference> reference1 = new ArrayList<>();
            reference1.add(new Reference("Russell, S., & Norvig, P. (2021). Artificial Intelligence: A Modern Approach. Pearson."));
            reference1.add(new Reference("Tegmark, M. (2017). Life 3.0: Being Human in the Age of Artificial Intelligence. Knopf."));
            Document document1 = new Document("The Future of Artificial Intelligence","Artificial intelligence (AI) is transforming industries and society at large. From healthcare to transportation, AI is driving innovation and efficiency. This document explores the potential future impacts of AI, including ethical considerations and technological advancements. The rapid pace of AI development necessitates thoughtful discourse on its role in society.",
                                reference1);
            document1.setAuthors(authorList.subList(0,3));

            ArrayList<Reference> reference2 = new ArrayList<>();
            reference2.add(new Reference("IPCC. (2021). Climate Change 2021: The Physical Science Basis. Cambridge University Press."));
            reference2.add(new Reference("Klein, N. (2014). This Changes Everything: Capitalism vs. The Climate. Simon & Schuster."));
            reference2.add(new Reference("McKibben, B. (2019). Falter: Has the Human Game Begun to Play Itself Out? Henry Holt and Co."));
            Document document2 = new Document("Climate Change: Challenges and Solutions","Climate change poses significant challenges to ecosystems, economies, and communities worldwide. This document examines the causes of climate change and potential solutions. It highlights the importance of international cooperation, sustainable practices, and technological innovation in addressing environmental issues. The document also discusses the role of policy and individual actions in mitigating climate impacts.",
                                reference2);
            document2.setAuthors(authorList.subList(2,5));

            ArrayList<Reference> reference3 = new ArrayList<>();
            reference3.add(new Reference("Castells, M. (2010). The Rise of the Network Society: The Information Age: Economy, Society, and Culture. Wiley-Blackwell."));
            reference3.add(new Reference("Schmidt, E., & Cohen, J. (2014). The New Digital Age: Reshaping the Future of People, Nations, and Business. Vintage"));
            Document document3 = new Document("The Evolution of the Internet","The internet has evolved from a research project to a global communication network. This document traces the history of the internet, from its inception to its current state. It discusses key milestones, such as the development of the World Wide Web and the rise of social media. The document also explores future trends, including the impact of the internet on privacy, security, and connectivity.",
                                reference3);
            document3.setAuthors(authorList.subList(6,8));

            documentRepository.save(document1);
            documentRepository.save(document2);
            documentRepository.save(document3);
        };
    } 
}
