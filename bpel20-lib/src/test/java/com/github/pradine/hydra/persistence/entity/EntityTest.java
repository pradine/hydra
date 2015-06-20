package com.github.pradine.hydra.persistence.entity;

/*
 * #%L
 * bpel20-lib
 * %%
 * Copyright (C) 2015 the original author or authors.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XdmNode;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pradine.hydra.persistence.entity.ActivityState.State;
import com.github.pradine.hydra.persistence.entity.Correlation2.Correlation2PK;
import com.github.pradine.hydra.persistence.entity.Correlation3.Correlation3PK;
import com.github.pradine.hydra.persistence.entity.MessageExchange.MessageExchangePK;
import com.github.pradine.hydra.persistence.entity.OpenIMA.OpenIMAPK;
import com.github.pradine.hydra.persistence.entity.PartnerLink.PartnerLinkPK;
import com.github.pradine.hydra.persistence.entity.Type.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EntityTest {
    private static final String EPR =
            "<sref:service-ref xmlns:sref=\"http://docs.oasis-open.org/wsbpel/2.0/serviceref\">"
            + "<foo:barEPR xmlns:foo=\"http://example.org\">http://server.github.com/service</foo:barEPR>"
            + "</sref:service-ref>";
    
    @Resource
    private Processor xmlProcessor;

    @PersistenceUnit(unitName = "entityManagerFactory")
    private EntityManagerFactory emf;
    
    private EntityManager em;
    
    @Before
    public void setUp() {
        if (emf != null)
            em = emf.createEntityManager();        
    }
    
    @After
    public void tearDown() {
        if (em != null)
            em.close();
    }
    
    @Test
    public void testScopeContext() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();
        
        Long context1Id = context1.getId();
        Long context2Id = context2.getId();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, context1Id);
        
        assertNotNull(context1);
        assertThat(context1.getScopeId(), is("bob"));
        assertFalse(context1.isIsolated());
        assertNull(context1.getParent());
        
        context2 = context1.getChild("fred");
        
        assertNotNull(context2);
        assertEquals(context1.getProcessId(), context2.getProcessId());
        assertEquals(context1, context2.getParent());
        assertEquals(context2Id, context2.getId());
        assertFalse(context2.isIsolated());
    }
    
    @Test
    public void testVariable() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        Variable variable1 = new Variable("Jack");
        Variable variable2 = new Variable("Jill");
        Variable variable3 = new Variable("Jack");
        
        QName qname1 = new QName("http://server.github.com/test", "type1");
        QName qname2 = new QName("http://server.github.com/test", "type2");
        QName qname3 = new QName("http://server.github.com/test", "type3");
        
        Type type1 = new Type(qname1, Category.WSDL_MESSAGE);
        Type type2 = new Type(qname2, Category.XSD_TYPE);
        Type type3 = new Type(qname3, Category.XSD_ELEMENT);
        
        variable1.setCopyOnWrite(false);
        variable1.setType(type1);
        variable2.setCopyOnWrite(false);
        variable2.setType(type2);
        variable3.setCopyOnWrite(false);
        variable3.setType(type3);
        
        context1.addVariable(variable1);
        context1.addVariable(variable2);
        context2.addVariable(variable3);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();

        Long context2Id = context2.getId();
        
        em.clear();
        
        context2 = em.find(ScopeContext.class, context2Id);
        
        variable1 = context2.getVariable("Jack");
        variable2 = context2.getVariable("Jill");
        variable3 = context2.getVariable("Jack");
        
        assertNotNull(variable1);
        assertNotNull(variable2);
        assertNotNull(variable3);
        assertNotNull(variable1.getScopeContext());
        assertNotNull(variable2.getScopeContext());
        assertNotNull(variable3.getScopeContext());
        assertEquals(variable1.getScopeContext(), variable3.getScopeContext());
        assertNotEquals(variable1.getScopeContext(), variable2.getScopeContext());
        assertFalse(variable1.isCopyOnWrite());
        assertFalse(variable2.isCopyOnWrite());
        assertEquals(type3, variable1.getType());
        assertEquals(type2, variable2.getType());
        assertEquals(new Long(1), variable3.getVersion());
        
        tran = em.getTransaction();
        tran.begin();
        
        variable3.setCopyOnWrite(true);
        
        tran.commit();
        
        assertTrue(variable3.isCopyOnWrite());
        assertEquals(new Long(2), variable3.getVersion());
    }
    
    @Test
    public void testMessageExchange() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        MessageExchange me1 = new MessageExchange("Jack");
        MessageExchange me2 = new MessageExchange("Jill");
        MessageExchange me3 = new MessageExchange("Jack");
        
        context1.addMessageExchange(me1);
        context1.addMessageExchange(me2);
        context2.addMessageExchange(me3);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();

        Long context2Id = context2.getId();
        
        em.clear();
        
        context2 = em.find(ScopeContext.class, context2Id);
        
        me1 = context2.getMessageExchange("Jack");
        me2 = context2.getMessageExchange("Jill");
        me3 = context2.getMessageExchange("Jack");
        
        assertNotNull(me1);
        assertNotNull(me2);
        assertNotNull(me3);
        assertNotNull(me1.getScopeContext());
        assertNotNull(me2.getScopeContext());
        assertNotNull(me3.getScopeContext());
        assertEquals(me1.getScopeContext(), me3.getScopeContext());
        assertNotEquals(me1.getScopeContext(), me2.getScopeContext());
    }
    
    @Test
    public void testPartnerLink() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        byte[] bytes = EPR.getBytes();
        DocumentBuilder builder = xmlProcessor.newDocumentBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Source source = new StreamSource(bais);
        XdmNode node = builder.build(source);

        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        PartnerLink partnerLink1 = new PartnerLink("Jack");
        PartnerLink partnerLink2 = new PartnerLink("Jill");
        PartnerLink partnerLink3 = new PartnerLink("Jack");
        
        QName qname1 = new QName("http://server.github.com/test", "type1");
        QName qname2 = new QName("http://server.github.com/test", "type2");
        QName qname3 = new QName("http://server.github.com/test", "type3");
        
        partnerLink1.setCopyOnWrite(false);
        partnerLink1.setTypeName(qname1);
        partnerLink1.setMyRole("bob");
        partnerLink1.setPartnerRole("fred");
        partnerLink1.setMyEPR(node);
        partnerLink2.setCopyOnWrite(false);
        partnerLink2.setTypeName(qname2);
        partnerLink2.setMyRole("bob");
        partnerLink2.setPartnerRole("fred");
        partnerLink2.setMyEPR(node);
        partnerLink3.setCopyOnWrite(false);
        partnerLink3.setTypeName(qname3);
        partnerLink3.setMyRole("bob");
        partnerLink3.setPartnerRole("fred");
        partnerLink3.setMyEPR(node);
        
        context1.addPartnerLink(partnerLink1);
        context1.addPartnerLink(partnerLink2);
        context2.addPartnerLink(partnerLink3);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();

        Long context2Id = context2.getId();
        
        em.clear();
        
        context2 = em.find(ScopeContext.class, context2Id);
        
        partnerLink1 = context2.getPartnerLink("Jack");
        partnerLink2 = context2.getPartnerLink("Jill");
        partnerLink3 = context2.getPartnerLink("Jack");
        
        assertNotNull(partnerLink1);
        assertNotNull(partnerLink2);
        assertNotNull(partnerLink3);
        assertNotNull(partnerLink1.getScopeContext());
        assertNotNull(partnerLink2.getScopeContext());
        assertNotNull(partnerLink3.getScopeContext());
        assertEquals(partnerLink1.getScopeContext(), partnerLink3.getScopeContext());
        assertNotEquals(partnerLink1.getScopeContext(), partnerLink2.getScopeContext());
        assertFalse(partnerLink1.isCopyOnWrite());
        assertFalse(partnerLink2.isCopyOnWrite());
        assertEquals("bob", partnerLink1.getMyRole());
        assertEquals("bob", partnerLink2.getMyRole());
        assertEquals("fred", partnerLink1.getPartnerRole());
        assertEquals("fred", partnerLink2.getPartnerRole());
        assertEquals(qname3, partnerLink1.getTypeName());
        assertEquals(qname2, partnerLink2.getTypeName());
        assertNull(partnerLink1.getPartnerEPR());
        assertNull(partnerLink2.getPartnerEPR());
        assertNull(partnerLink1.isInitializePartnerRole());
        assertNull(partnerLink2.isInitializePartnerRole());
        assertXMLEqual(EPR, partnerLink1.getMyEPR().toString());
        assertXMLEqual(EPR, partnerLink2.getMyEPR().toString());
        assertEquals(new Long(1), partnerLink3.getVersion());
        
        tran = em.getTransaction();
        tran.begin();
        
        partnerLink3.setCopyOnWrite(true);
        
        tran.commit();
        
        assertTrue(partnerLink3.isCopyOnWrite());
        assertEquals(new Long(2), partnerLink3.getVersion());
    }
    
    @Test
    public void testCorrelationSet() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        List<QName> properties1 = new ArrayList<QName>();
        List<QName> properties2 = new ArrayList<QName>();
        List<QName> properties3 = new ArrayList<QName>();
        
        properties1.add(new QName("http://server.github.com", "ginger"));
        properties2.add(new QName("http://server.github.com", "ginger"));
        properties2.add(new QName("http://server.github.com", "william"));
        properties3.add(new QName("http://server.github.com", "roger"));
        properties3.add(new QName("http://server.github.com", "florence"));
        
        CorrelationSet correlationSet1 = new CorrelationSet("Jack");
        CorrelationSet correlationSet2 = new CorrelationSet("Jill");
        CorrelationSet correlationSet3 = new CorrelationSet("Jack");
        
        correlationSet1.setCorrelation(Correlation1.class);
        correlationSet1.setCorrelationPK(null);
        correlationSet1.setProperties(properties1);
        correlationSet2.setCorrelation(Correlation2.class);
        correlationSet2.setCorrelationPK(Correlation2PK.class);
        correlationSet2.setProperties(properties2);
        correlationSet3.setCorrelation(Correlation3.class);
        correlationSet3.setCorrelationPK(Correlation3PK.class);
        correlationSet3.setProperties(properties3);
        
        context1.addCorrelationSet(correlationSet1);
        context1.addCorrelationSet(correlationSet2);
        context2.addCorrelationSet(correlationSet3);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();
        
        Long context2Id = context2.getId();
        
        em.clear();
        
        context2 = em.find(ScopeContext.class, context2Id);
        
        correlationSet1 = context2.getCorrelationSet("Jack");
        correlationSet2 = context2.getCorrelationSet("Jill");
        correlationSet3 = context2.getCorrelationSet("Jack");

        assertNotNull(correlationSet1);
        assertNotNull(correlationSet2);
        assertNotNull(correlationSet3);
        assertNotNull(correlationSet1.getScopeContext());
        assertNotNull(correlationSet2.getScopeContext());
        assertNotNull(correlationSet3.getScopeContext());
        assertEquals(correlationSet1.getScopeContext(), correlationSet3.getScopeContext());
        assertNotEquals(correlationSet1.getScopeContext(), correlationSet2.getScopeContext());
        assertEquals(Correlation3.class, correlationSet1.getCorrelation());
        assertEquals(Correlation2.class, correlationSet2.getCorrelation());
        assertEquals(Correlation3PK.class, correlationSet1.getCorrelationPK());
        assertEquals(Correlation2PK.class, correlationSet2.getCorrelationPK());
        assertEquals(properties3, correlationSet1.getProperties());
        assertEquals(properties2, correlationSet2.getProperties());
        assertEquals(new Long(1), correlationSet3.getVersion());
        
        tran = em.getTransaction();
        tran.begin();
        
        List<QName> list = new ArrayList<QName>();
        correlationSet3.setProperties(list);
        
        tran.commit();
        
        assertEquals(list, correlationSet3.getProperties());
        assertEquals(new Long(2), correlationSet3.getVersion());
    }
    
    @Test
    public void testFlowContext() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        
        FlowContext flow1 = new FlowContext("Tom");
        FlowContext flow2 = new FlowContext("Jerry");
        
        context1.addFlowContext(flow1);
        context2.addFlowContext(flow2);
        flow1.addChild(flow2);
        
        em.persist(flow1);
        tran.commit();
        
        Long context1Id = context1.getId();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, context1Id);
        context2 = context1.getChild("fred");
        flow1 = context1.getFlowContext("Tom");
        flow2 = flow1.getChild(context2.getId(), "Jerry");
        
        assertNotNull(flow1);
        assertNotNull(flow2);
        assertNull(flow1.getParent());
        assertNotNull(flow2.getParent());
        assertEquals(new Integer(0), flow1.getCounter());
        assertEquals(new Integer(0), flow2.getCounter());
    }
    
    @Test
    public void testLink() {
        ScopeContext context1 = new ScopeContext();
        ScopeContext context2 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        context2.setScopeId("fred");
        context2.setIsolated(false);
        context1.addChild(context2);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        
        FlowContext flow1 = new FlowContext("Tom");
        FlowContext flow2 = new FlowContext("Jerry");
        
        context1.addFlowContext(flow1);
        context2.addFlowContext(flow2);
        flow1.addChild(flow2);
        
        Link link1 = new Link("Jack");
        Link link2 = new Link("Jill");
        Link link3 = new Link("Jack");
        
        link1.setCopyOnWrite(false);
        link1.setState(true);
        link2.setCopyOnWrite(false);
        link2.setState(null);
        link3.setCopyOnWrite(false);
        link3.setState(false);
        
        flow1.addLink(link1);
        flow1.addLink(link2);
        flow2.addLink(link3);
        
        em.persist(flow1);
        tran.commit();
        
        Long context2Id = context2.getId();
        
        em.clear();
        
        context2 = em.find(ScopeContext.class, context2Id);
        flow2 = context2.getFlowContext("Jerry");
        
        link1 = flow2.getLink("Jack");
        link2 = flow2.getLink("Jill");
        link3 = flow2.getLink("Jack");
        
        assertNotNull(link1);
        assertNotNull(link2);
        assertNotNull(link3);
        assertNotNull(link1.getFlowContext());
        assertNotNull(link2.getFlowContext());
        assertNotNull(link3.getFlowContext());
        assertEquals(link1.getFlowContext(), link3.getFlowContext());
        assertNotEquals(link1.getFlowContext(), link2.getFlowContext());
        assertFalse(link1.isCopyOnWrite());
        assertFalse(link2.isCopyOnWrite());
        assertFalse(link1.getState());
        assertNull(link2.getState());
        assertEquals(new Long(1), link3.getVersion());
        
        tran = em.getTransaction();
        tran.begin();
        
        link3.setCopyOnWrite(true);
        
        tran.commit();
        
        assertTrue(link3.isCopyOnWrite());
        assertEquals(new Long(2), link3.getVersion());
    }
    
    @Test
    public void testCorrelation() {
        UUID processId = UUID.randomUUID();
        List<AbstractCorrelation> list = new ArrayList<AbstractCorrelation>();

        Correlation1 correlation1 = new Correlation1(500d);
        Correlation2 correlation2 = new Correlation2(250d, 50d);
        Correlation3 correlation3 = new Correlation3(5l, "charlie");
        
        list.add(correlation1);
        list.add(correlation2);
        list.add(correlation3);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        for (AbstractCorrelation correlation : list) {
            correlation.setProcessId(processId);
            em.persist(correlation);
        }
        
        tran.commit();
        em.clear();
        
        Correlation2PK correlation2PK = new Correlation2PK(250d, 50d);
        Correlation3PK correlation3PK = new Correlation3PK(5l, "charlie");
        
        correlation1 = em.find(Correlation1.class, 500d);
        correlation2 = em.find(Correlation2.class, correlation2PK);
        correlation3 = em.find(Correlation3.class, correlation3PK);
        
        assertEquals(processId, correlation1.getProcessId());
        assertEquals(processId, correlation2.getProcessId());
        assertEquals(processId, correlation3.getProcessId());
    }
    
    @Test
    public void testActivityState() {
        ScopeContext context1 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        
        ActivityState activityState = new ActivityState("fred");
        
        activityState.setActivityName("ginger");
        activityState.setState(State.READY);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        
        context1.addActivityState(activityState);
        
        em.persist(activityState);
        tran.commit();
        
        Long context1Id = context1.getId();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, context1Id);
        activityState = context1.getActivityState("fred");
        
        assertNotNull(activityState);
        assertNotNull(activityState.getScopeContext());
        assertThat("ginger", is(activityState.getActivityName()));
        assertEquals(State.READY, activityState.getState());
        assertNotNull(activityState.getInitialUpdate());
        assertNotNull(activityState.getLastUpdate());
        assertEquals(activityState.getInitialUpdate(), activityState.getLastUpdate());
        assertEquals(new Long(1), activityState.getVersion());
        
        tran = em.getTransaction();
        tran.begin();
        
        activityState.setState(State.ACTIVE);
        
        tran.commit();
        
        assertEquals(State.ACTIVE, activityState.getState());
        assertNotEquals(activityState.getInitialUpdate(), activityState.getLastUpdate());
        assertEquals(new Long(2), activityState.getVersion());
    }

    @Test
    public void testData() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        byte[] bytes = EPR.getBytes();
        DocumentBuilder builder = xmlProcessor.newDocumentBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Source source = new StreamSource(bais);
        XdmNode node = builder.build(source);

        Data data1 = new Data();
        Data data2 = new Data();
        Data data3 = new Data();
        
        data1.setNode(node);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(data1);
        tran.commit();
        
        Long data1Id = data1.getId();
        
        em.clear();
        
        data1 = em.find(Data.class, data1Id);
        
        assertNotNull(data1);
        assertNull(data1.getPartName());
        assertNull(data1.getParent());
        
        Map<String, Data> map = data1.getChildren();
        
        assertNotNull(map);
        assertTrue(map.isEmpty());
        assertXMLEqual(EPR, data1.getNode().toString());
        
        tran = em.getTransaction();
        tran.begin();
        
        em.remove(data1);
        
        tran.commit();
        tran = em.getTransaction();
        tran.begin();
                
        data1 = new Data();
        
        data2.setNode(node);
        data2.setPartName("part2");
        data3.setNode(node);
        data3.setPartName("part3");
        
        data1.addChild(data2);
        data1.addChild(data3);
        
        em.persist(data1);
        
        tran.commit();
        
        data1Id = data1.getId();
        
        em.clear();
        
        data1 = em.find(Data.class, data1Id);
        
        assertNotNull(data1);
        assertNull(data1.getPartName());
        assertNull(data1.getParent());
        
        map = data1.getChildren();
        
        assertNotNull(map);
        assertEquals(2, map.size());
        assertNull(data1.getNode());
        
        for (String key : map.keySet()) {
            Data data = map.get(key);
            
            assertNotNull(data);
            assertEquals(key, data.getPartName());
            assertNotNull(data.getParent());
            
            Map<String, Data> children = data.getChildren();
            
            assertNotNull(children);
            assertTrue(children.isEmpty());
            assertXMLEqual(EPR, data.getNode().toString());
        }
        
        assertEquals(data2.getParent(), data3.getParent());
        
        em.clear();
        
        try {
            data1 = em.find(Data.class, data1Id);

            tran = em.getTransaction();
            tran.begin();
            data2 = data1.getChild("part2");
            data2.setPartName(null);
            
            em.persist(data1);
            
            tran.commit();
            
            fail("Expected a RollbackException to be thrown.");
        }
        catch (RollbackException re) {
            assertTrue(re.getCause() instanceof AssertionError);
            assertThat(re.getCause().getMessage(),
                    is("The return value from Data.getPartName() is null."));
        }
        finally {
            if (tran != null && tran.isActive())
                tran.rollback();
            
            if (em != null && em.isOpen())
                em.clear();
        }

        try {
            data1 = em.find(Data.class, data1Id);

            tran = em.getTransaction();
            tran.begin();
            Data data4 = new Data();
            data4.setPartName("part4");
            data2 = data1.getChild("part2");
            data2.addChild(data4);
            
            em.persist(data1);
            
            tran.commit();
            
            fail("Expected a RollbackException to be thrown.");
        }
        catch (RollbackException re) {
            assertTrue(re.getCause() instanceof AssertionError);
            assertThat(re.getCause().getMessage(),
                    is("The return value from Data.getChildren() is not empty, or null."));
        }
        finally {
            if (tran != null && tran.isActive())
                tran.rollback();
            
            if (em != null && em.isOpen())
                em.clear();
        }
        
        try {
            data1 = em.find(Data.class, data1Id);

            tran = em.getTransaction();
            tran.begin();
            data1.setPartName("error");
            
            em.persist(data1);
            
            tran.commit();
            
            fail("Expected a RollbackException to be thrown.");
        }
        catch (RollbackException re) {
            assertTrue(re.getCause() instanceof AssertionError);
            assertThat(re.getCause().getMessage(),
                    is("The return value from Data.getPartName() is not null."));
        }
        finally {
            if (tran != null && tran.isActive())
                tran.rollback();
            
            if (em != null && em.isOpen())
                em.clear();
        }
        
        try {
            data1 = em.find(Data.class, data1Id);

            tran = em.getTransaction();
            tran.begin();
            data1.setNode(node);
            
            em.persist(data1);
            
            tran.commit();
            
            fail("Expected a RollbackException to be thrown.");
        }
        catch (RollbackException re) {
            assertTrue(re.getCause() instanceof AssertionError);
            assertThat(re.getCause().getMessage(),
                    is("The return value from Data.getNode() is not null."));
        }
        finally {
            if (tran != null && tran.isActive())
                tran.rollback();
            
            if (em != null && em.isOpen())
                em.clear();
        }
        
        data1 = em.find(Data.class, data1Id);

        tran = em.getTransaction();
        tran.begin();
        
        em.remove(data1);
        
        tran.commit();
        em.clear();
        
        TypedQuery<Data> query = em.createQuery("select d from Data d", Data.class);
        List<Data> list = query.getResultList();
        
        assertEquals(0, list.size());
    }
    
    @Test
    public void testDestinations() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        byte[] bytes = EPR.getBytes();
        DocumentBuilder builder = xmlProcessor.newDocumentBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Source source = new StreamSource(bais);
        XdmNode node = builder.build(source);

        ScopeContext context1 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");
        
        Variable variable1 = new Variable("Jack");
        
        QName qname1 = new QName("http://server.github.com/test", "type1");
        
        Type type1 = new Type(qname1, Category.XSD_ELEMENT);
        
        variable1.setCopyOnWrite(false);
        variable1.setType(type1);
        
        context1.addVariable(variable1);
        
        Data data1 = new Data();
        
        data1.setNode(node);
        
        InboundDestination inDest = new InboundDestination();
        
        inDest.setProcessId(context1.getProcessId());
        inDest.setActivityId("fred");
        inDest.setData(data1);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        em.persist(inDest);
        tran.commit();
        
        Long contextId = context1.getId();
        Long inDestId = inDest.getId();

        em.clear();
        
        context1 = em.find(ScopeContext.class, contextId);
        inDest = em.find(InboundDestination.class, inDestId);
        
        assertEquals(context1.getProcessId(), inDest.getProcessId());
        assertThat(inDest.getActivityId(), is("fred"));
        assertNotNull(inDest.getArrivalTime());
        assertXMLEqual(EPR, inDest.getData().getNode().toString());
        
        tran = em.getTransaction();
        tran.begin();
        
        variable1 = context1.getVariable("Jack");
        
        variable1.setData(inDest.getData());
        
        em.persist(variable1);
        em.remove(inDest);
        
        tran.commit();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, contextId);
        inDest = em.find(InboundDestination.class, inDestId);
        
        assertNotNull(context1);
        assertNull(inDest);
        
        variable1 = context1.getVariable("Jack");
        data1 = variable1.getData();
        
        assertNotNull(data1);
        assertXMLEqual(EPR, data1.getNode().toString());
        
        tran = em.getTransaction();
        tran.begin();
        
        OutboundDestination outDest = new OutboundDestination();
        
        outDest.setData(data1);
        
        em.persist(outDest);
        
        tran.commit();
        
        Long outDestId = outDest.getId();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, contextId);
        outDest = em.find(OutboundDestination.class, outDestId);
        
        assertNotNull(outDest.getDepartureTime());
        assertXMLEqual(EPR, outDest.getData().getNode().toString());

        tran = em.getTransaction();
        tran.begin();
        
        em.remove(outDest);
        
        tran.commit();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, contextId);
        outDest = em.find(OutboundDestination.class, outDestId);
        
        assertNotNull(context1);
        assertNull(outDest);
        
        variable1 = context1.getVariable("Jack");
        data1 = variable1.getData();
        
        assertNotNull(data1);
        assertXMLEqual(EPR, data1.getNode().toString());        
    }
    
    @Test
    public void testOpenIMA() throws Exception {
        byte[] bytes = EPR.getBytes();
        DocumentBuilder builder = xmlProcessor.newDocumentBuilder();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Source source = new StreamSource(bais);
        XdmNode node = builder.build(source);

        ScopeContext context1 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");

        PartnerLink partnerLink1 = new PartnerLink("Jack");
        PartnerLink partnerLink2 = new PartnerLink("Jill");
        
        QName qname1 = new QName("http://server.github.com/test", "type1");
        QName qname2 = new QName("http://server.github.com/test", "type2");
        
        partnerLink1.setCopyOnWrite(false);
        partnerLink1.setTypeName(qname1);
        partnerLink1.setMyRole("bob");
        partnerLink1.setPartnerRole("fred");
        partnerLink1.setMyEPR(node);
        partnerLink2.setCopyOnWrite(false);
        partnerLink2.setTypeName(qname2);
        partnerLink2.setMyRole("bob");
        partnerLink2.setPartnerRole("fred");
        partnerLink2.setMyEPR(node);
        
        context1.addPartnerLink(partnerLink1);
        context1.addPartnerLink(partnerLink2);
        
        MessageExchange me1 = new MessageExchange("Jack");
        MessageExchange me2 = new MessageExchange("Jill");
        
        context1.addMessageExchange(me1);
        context1.addMessageExchange(me2);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        
        OpenIMA openIMA1 = new OpenIMA(partnerLink1, "operation1", me1);
        OpenIMA openIMA2 = new OpenIMA(partnerLink2, "operation2", me2);
        
        em.persist(openIMA1);
        em.persist(openIMA2);
        tran.commit();
        
        em.clear();
        
        openIMA1 = em.find(OpenIMA.class,
                new OpenIMAPK(new PartnerLinkPK(context1.getId(), partnerLink1.getName()),
                        "operation1",
                        new MessageExchangePK(context1.getId(), me1.getName()))
        );
        openIMA2 = em.find(OpenIMA.class,
                new OpenIMAPK(new PartnerLinkPK(context1.getId(), partnerLink2.getName()),
                        "operation2",
                        new MessageExchangePK(context1.getId(), me2.getName()))
        );
        
        assertNotNull(openIMA1);
        assertNotNull(openIMA2);
    }
    
    @Test
    public void testOwner() {
        Owner owner = new Owner("John");
        
        owner.setActiveId("John");
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(owner);
        tran.commit();
        
        em.clear();
        
        owner = em.find(Owner.class, "John");
        
        assertNotNull(owner);
        assertEquals("John", owner.getActiveId());
        assertNotNull(owner.getInitialUpdate());
        assertNotNull(owner.getLastUpdate());
        assertEquals(owner.getInitialUpdate(), owner.getLastUpdate());
        assertEquals(new Integer(0), owner.getMajorNumber());
        assertEquals(new Integer(1), owner.getMinorNumber());
    }
    
    @Test
    public void testTimer() {
        ScopeContext context1 = new ScopeContext();
        
        context1.setProcessId(UUID.randomUUID());
        context1.setIsolated(false);
        context1.setScopeId("bob");

        Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        
        timer1.setExpiryTime(Calendar.getInstance());
        timer1.setOriginatorId("fred");
        timer1.setFired(false);
        timer2.setExpiryTime(Calendar.getInstance());
        timer2.setOriginatorId("fred");
        timer2.setFired(false);
        
        context1.addTimer(timer1);
        context1.addTimer(timer2);
        
        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(context1);
        tran.commit();
        
        Long context1Id = context1.getId();
        
        em.clear();
        
        context1 = em.find(ScopeContext.class, context1Id);
        List<Timer> timers = context1.getTimers();
        
        assertEquals(2, timers.size());
        
        timer1 = timers.get(0);
        timer2 = timers.get(1);
        
        assertNotNull(timer1);
        assertNotNull(timer2);
        assertEquals(new Long(1), timer1.getId());
        assertEquals(new Long(2), timer2.getId());
        assertNotNull(timer1.getExpiryTime());
        assertNotNull(timer2.getExpiryTime());
        assertEquals("fred", timer1.getOriginatorId());
        assertEquals("fred", timer2.getOriginatorId());
        assertNotNull(timer1.getScopeContext());
        assertNotNull(timer2.getScopeContext());
        assertEquals(timer2.getScopeContext(), timer1.getScopeContext());
        assertFalse(timer1.isFired());
        assertFalse(timer2.isFired());
        assertNull(timer1.getRepeatDuration());
        assertNull(timer2.getRepeatDuration());
        
        tran = em.getTransaction();
        tran.begin();
        
        timers.set(0, null);
        
        tran.commit();
        em.clear();
        
        TypedQuery<Timer> query = em.createQuery("select t from Timer t", Timer.class);
        timers = query.getResultList();
        
        assertEquals(1, timers.size());
        
        timer1 = timers.get(0);
        
        assertNotNull(timer1);
        assertEquals(new Long(2), timer1.getId());
    }
    
    @Test
    public void testWSDL11ToIMA() {
        WSDL11ToIMA toIMA = new WSDL11ToIMA();
        List<IMA> list = new ArrayList<IMA>();
        
        QName qname = new QName("http://server.github.com", "ginger");
        
        IMA ima1 = new IMA("fred");
        IMA ima2 = new IMA("bob");
        
        list.add(ima1);
        list.add(ima2);
        
        toIMA.setIMAs(list);
        toIMA.setOperation("henry");
        toIMA.setPortType(qname);

        EntityTransaction tran = em.getTransaction();
        tran.begin();
        
        em.persist(toIMA);
        tran.commit();
        
        Long toIMAId = toIMA.getId();
        
        em.clear();
        
        toIMA = em.find(WSDL11ToIMA.class, toIMAId);
        list = toIMA.getIMAs();
        
        assertNotNull(toIMA);
        assertEquals("henry", toIMA.getOperation());
        assertEquals(qname, toIMA.getPortType());
        assertEquals(new Long(1), toIMA.getVersion());
        assertNotNull(list);
        assertEquals(2, list.size());
        
        ima1 = list.get(0);
        ima2 = list.get(1);
        
        assertEquals("fred", ima1.getActivityId());
        assertEquals("bob", ima2.getActivityId());
        assertEquals(new Integer(0), ima1.getCounter());
        assertEquals(new Integer(0), ima2.getCounter());
        
        tran = em.getTransaction();
        tran.begin();
        
        ima1.increment();
        ima2.decrement();
        
        tran.commit();
        em.clear();
        
        toIMA = em.find(WSDL11ToIMA.class, toIMAId);
        list = toIMA.getIMAs();

        ima1 = list.get(0);
        ima2 = list.get(1);
        
        assertEquals(new Integer(1), ima1.getCounter());
        assertEquals(new Integer(-1), ima2.getCounter());
        assertEquals(new Long(2), toIMA.getVersion());
    }
}
