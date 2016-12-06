package agentville.games.wumpus.world;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class WumpusPerceptionOntology extends Ontology {
	
	private static final long serialVersionUID = -2346795100440706323L;
	
	public static final String ONTOLOGY_NAME = "Perception-Ontology";
	public static final String TYPE_NAME = "Perception";
    public static final String PERCEPTION_STENCH = WumpusConsts.SENSOR_STENCH;//"stench";
    public static final String PERCEPTION_BREEZE = WumpusConsts.SENSOR_BREEZE;//"breeze";
    public static final String PERCEPTION_GLITTER = WumpusConsts.SENSOR_GLITTER;//"glitter";
    public static final String PERCEPTION_STATE = "state";
    public static final String PERCEPTION_BUMP = WumpusConsts.SENSOR_BUMP;//"bump";
    public static final String PERCEPTION_SCREAM = WumpusConsts.SENSOR_SCREAM;//"scream";
    private static Ontology theInstance = new WumpusPerceptionOntology();
	
	
	public WumpusPerceptionOntology() {
		
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
        try
        {
            add(new PredicateSchema(TYPE_NAME), WumpusPerception.class);
            
            PredicateSchema ps = (PredicateSchema)getSchema(TYPE_NAME);
            ps.add(PERCEPTION_STENCH, (PrimitiveSchema)getSchema(BasicOntology.STRING));
            ps.add(PERCEPTION_BREEZE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
            ps.add(PERCEPTION_GLITTER, (PrimitiveSchema)getSchema(BasicOntology.STRING));
            ps.add(PERCEPTION_BUMP, (PrimitiveSchema)getSchema(BasicOntology.STRING));
            ps.add(PERCEPTION_SCREAM, (PrimitiveSchema)getSchema(BasicOntology.STRING));
            ps.add(PERCEPTION_STATE, (PrimitiveSchema)getSchema(BasicOntology.STRING));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
	}
    public static Ontology getInstance()
    {
        return theInstance;
    }
}
